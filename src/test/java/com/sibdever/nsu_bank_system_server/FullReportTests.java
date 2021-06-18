package com.sibdever.nsu_bank_system_server;

import com.sibdever.nsu_bank_system_server.data.model.entities.*;
import com.sibdever.nsu_bank_system_server.data.repo.*;
import com.sibdever.nsu_bank_system_server.exception.WrongCredentialsException;
import com.sibdever.nsu_bank_system_server.service.ClientsManagementService;
import com.sibdever.nsu_bank_system_server.service.CrudOfferService;
import com.sibdever.nsu_bank_system_server.service.DailyScheduledService;
import com.sibdever.nsu_bank_system_server.service.PaymentsManagementService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

// Custom test config!
@SpringBootTest(classes = NsuBankSystemServerApplication.class)
@TestPropertySource(locations = "classpath:original-database-test.properties")
public class FullReportTests {

    static Client testClient1;
    static Client testClient2;
    static Offer testOffer;
    static Credit testCredit1;
    static Credit testCredit2;

    @Autowired
    private FullReportRepoImpl fullReportRepo;
    @Autowired
    private DailyScheduledService dailyScheduledService;
    @Autowired
    private PaymentsManagementService paymentsManagementService;
    @Autowired
    private DayStatisticRepo dayStatisticRepo;
    @Autowired
    private CreditTableRepo creditTableRepo;
    @Autowired
    private CreditHistoryRepo creditHistoryRepo;

    @BeforeAll
    @Rollback(false)
    public static void init(
            @Autowired ClientsRepo clientsRepo,
            @Autowired CrudOfferService crudOfferService,
            @Autowired ClientsManagementService clientsManagementService
    ) throws WrongCredentialsException {
        testClient1 = clientsRepo.saveAndFlush(new Client("TestFullName1"));
        testClient2 = clientsRepo.saveAndFlush(new Client("TestFullName2"));
        testOffer = crudOfferService.createOffer(new Offer("test", 1, 1, 100, 0, 1000000));
        clientsManagementService.setClientOffer(testClient1.getId(), testOffer.getId());
        clientsManagementService.setClientOffer(testClient2.getId(), testOffer.getId());
        testCredit1 = clientsManagementService.giveCredit(testClient1.getId(), testOffer.getId(), 12, 10000, PaymentChannel.CREDIT_CARD);
        testCredit2 = clientsManagementService.giveCredit(testClient2.getId(), testOffer.getId(), 12, 10000, PaymentChannel.CREDIT_CARD);
    }

    @Test
    @Transactional
    public void testFullReport() throws WrongCredentialsException {

        Clock clock = Clock.systemDefaultZone();
        payInFirsDay(clock);

        dailyScheduledService.manageDailyPayments();

        clock = Clock.offset(clock, Duration.ofDays(30));

        dailyScheduledService.setClock(clock);

        payAfterMonth(clock);

        dailyScheduledService.manageDailyPayments();

        clock = Clock.offset(clock, Duration.ofDays(10));

        dailyScheduledService.setClock(clock);

        payAfterTenDays(clock);

        dailyScheduledService.manageDailyPayments();

        logStatistic();

        System.out.println("RESULT: ");
        fullReportRepo.getFullReportTest().forEach(System.out::println);
    }

    // This tables are really correct!
    private void logStatistic() {
        System.out.println("Day stat:");
        dayStatisticRepo.findAll().forEach(record -> {
            System.out.println(record.getDate() + " " + record.getCredit().getId() + " " + record.getCredit().getBalance() + " " + record.getCashInflow() + " " + record.getProfitMargin());
        });
        System.out.println();

        System.out.println("Credit1:");
        creditTableRepo.findAllById_Credit_IdOrderById_Timestamp(testCredit1.getId())
                .forEach(record -> {
                    System.out.println(record.getId().getTimestamp() + " " + record.getExpectedPayout() + " " + record.getRealPayout() + " " + record.getCreditStatusAfterPayment() + " " + record.getBalanceAfterPayment());
                });

        System.out.println("Credit2:");
        creditTableRepo.findAllById_Credit_IdOrderById_Timestamp(testCredit2.getId())
                .forEach(record -> {
                    System.out.println(record.getId().getTimestamp() + " " + record.getExpectedPayout() + " " + record.getRealPayout() + " " + record.getCreditStatusAfterPayment() + " " + record.getBalanceAfterPayment());
                });
        System.out.println();

        System.out.println("History:");
        creditHistoryRepo.findAll().forEach(record -> {
            System.out.println(record.getTimestamp() + " " + record.getCredit().getId() + " " + record.getCreditStatus());
                }
        );
        System.out.println();
    }

    private void payInFirsDay(Clock clock) throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient1.getId(),
                testCredit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        400.0
                )
        );

        paymentsManagementService.processPayment(
                testClient1.getId(),
                testCredit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        500.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                testCredit2.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        300.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                testCredit2.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        700.0
                )
        );
    }

    public void payAfterMonth(Clock clock) throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient1.getId(),
                testCredit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        400.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                testCredit2.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        500.0
                )
        );
    }

    public void payAfterTenDays(Clock clock) throws WrongCredentialsException {
        paymentsManagementService.processPayment(
                testClient1.getId(),
                testCredit1.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        1000.0
                )
        );

        paymentsManagementService.processPayment(
                testClient2.getId(),
                testCredit2.getId(),
                new PaymentDetails(
                        LocalDateTime.now(clock),
                        PaymentType.REFUND,
                        PaymentChannel.BANK_ACCOUNT,
                        500.0
                )
        );
    }

}
