package com.sibdever.nsu_bank_system_server.service;

import com.sibdever.nsu_bank_system_server.data.model.entities.Credit;
import com.sibdever.nsu_bank_system_server.data.model.entities.Payment;
import com.sibdever.nsu_bank_system_server.data.repo.CreditTableRepo;
import com.sibdever.nsu_bank_system_server.data.repo.CreditsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class DailyScheduledService {

    @Autowired
    private CreditsManagementService creditsManagementService;
    @Autowired
    private CreditsRepo creditsRepo;
    @Autowired
    private CreditTableRepo creditTableRepo;

    private LocalDateTime lastLaunch = LocalDateTime.now();

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

        lastLaunch = LocalDateTime.now();

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

        creditTableRowsToCheck.forEach(row -> {

        });

    }

}
