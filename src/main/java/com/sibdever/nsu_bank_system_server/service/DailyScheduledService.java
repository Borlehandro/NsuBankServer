package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import com.sibdever.nsu_bank_system_server.data.repo.PaymentsRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class DailyScheduledService {

    private final CreditsManagementService creditsManagementService;
    private final CreditsRepo creditsRepo;
    private final CreditTableRepo creditTableRepo;
    private final PaymentsRepo paymentsRepo;
    
    private Clock clock;

    private LocalDateTime lastLaunch;

    public DailyScheduledService(CreditsManagementService creditsManagementService, CreditsRepo creditsRepo, CreditTableRepo creditTableRepo, PaymentsRepo paymentsRepo, Clock clock) {
        this.creditsManagementService = creditsManagementService;
        this.creditsRepo = creditsRepo;
        this.creditTableRepo = creditTableRepo;
        this.paymentsRepo = paymentsRepo;
        this.clock = clock;
        lastLaunch = LocalDateTime.now(clock);
    }

    // Todo use cron
    // @Scheduled(fixedDelay = 1000)
    @Transactional
    public void manageDailyPayments() {

        System.out.println("Last start:" + lastLaunch);
        var creditPayments = creditsRepo
                .findAllWherePaymentsNotHandled()
                .stream()
                .collect(Collectors.groupingBy(
                        (Object[] item) -> (Credit) item[0],
                        Collectors.mapping((Object[] item) -> (Payment) item[1], Collectors.toSet())
                ));

        System.out.println("PAYMENTS:");
        creditPayments.forEach((key, value) -> {
            System.out.println(key.getId() + " ");
            value.forEach(v -> System.out.println(v.getPaymentDetails().getTimestamp()));
        } );

        lastLaunch = LocalDateTime.now(clock);
        System.out.println("now : " + lastLaunch.format(DateTimeFormatter.ISO_DATE_TIME));

        creditPayments.forEach((credit, list) -> {
                    creditsManagementService.recalculateCreditTableWithDailyPayments(
                            credit,
                            list,
                            lastLaunch.toLocalDate()
                    );
                    list.forEach(pay -> pay.setHandled(true));
                }
        );
        paymentsRepo.flush();


        System.out.println("Between: " + lastLaunch.format(DateTimeFormatter.ISO_DATE_TIME)
                + "and"
                + lastLaunch.minus(1, ChronoUnit.MONTHS).format(DateTimeFormatter.ISO_DATE_TIME));

        // Todo use date
        var creditTableRowsToCheck = creditTableRepo
                .findAllInThisMonthWhereRealPayoutLessThanExpected(lastLaunch);

        System.out.println("\nTO CHECK");
        creditTableRowsToCheck.forEach(System.out::println);
        System.out.println("END TO CHECK\n");
        creditTableRowsToCheck.forEach(creditsManagementService::handleExpired);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
        creditsManagementService.setClock(clock);
    }
}