package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class DailyScheduledService {

    private final CreditsManagementService creditsManagementService;
    private final CreditsRepo creditsRepo;
    private final CreditTableRepo creditTableRepo;
    
    private final Clock clock;

    private LocalDateTime lastLaunch;

    public DailyScheduledService(CreditsManagementService creditsManagementService, CreditsRepo creditsRepo, CreditTableRepo creditTableRepo, Clock clock) {
        this.creditsManagementService = creditsManagementService;
        this.creditsRepo = creditsRepo;
        this.creditTableRepo = creditTableRepo;
        this.clock = clock;
        lastLaunch = LocalDateTime.now(clock);
    }

    // Todo use cron
    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void manageDailyPayments() {

        var creditPayments = creditsRepo
                .findAllJoinPaymentsAfterTime(lastLaunch)
                .stream()
                .collect(Collectors.groupingBy(
                        (Object[] item) -> (Credit) item[0],
                        Collectors.mapping((Object[] item) -> (Payment) item[1], Collectors.toSet())
                ));

        lastLaunch = LocalDateTime.now(clock);

        creditPayments.keySet().forEach(credit ->
                creditsManagementService.recalculateCreditTableWithDailyPayments(
                        credit,
                        creditPayments.get(credit),
                        lastLaunch.toLocalDate()
                )
        );

        // Todo use date
        var creditTableRowsToCheck = creditTableRepo.findAllWhereTimestampBetweenAndRealPayoutLessThanExpected(
                lastLaunch,
                lastLaunch.minus(1, ChronoUnit.MONTHS)
        );

        creditTableRowsToCheck.forEach(creditsManagementService::handleExpired);

    }

}
