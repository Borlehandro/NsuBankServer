package com.sibdever.nsu_bank_system_server.data.model.entities;

import com.sibdever.nsu_bank_system_server.data.model.ReportRecord;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor

@org.hibernate.annotations.NamedNativeQuery(
        name = "FullStatisticReportQuery",
        query = """
                SELECT date_part('day', day_statistic.date)   as day,
                             date_part('month', day_statistic.date) as month,
                             date_part('year', day_statistic.date)  as year,
                             sum(release_payments.payment_sum)         totalRelease,
                             sum(refund_payments.payment_sum)          totalRefund,
                             count(expired_credits)                    expiredCreditsPercent,
                             avg(day_statistic.profit_margin)          totalProfit,
                             0                                         profitPlus
                      from day_statistic
                               join payments release_payments
                                    on day_statistic.date = (release_payments.timestamp)::::date and release_payments.type = 0
                               join payments refund_payments
                                    on day_statistic.date = (release_payments.timestamp)::::date and refund_payments.type = 1
                               right outer join credits_history expired_credits
                                                on day_statistic.date = (expired_credits.timestamp)::::date and
                                                   expired_credits.credit_status = 2
                      group by grouping sets (date_part('day', day_statistic.date),
                                              date_part('month', day_statistic.date),
                                              date_part('year', day_statistic.date)
                          );
                """, resultSetMapping = "ReportRecord")

@SqlResultSetMapping(
        name = "ReportRecord",
        classes = {
                @ConstructorResult(
                        targetClass = ReportRecord.class,
                        columns = {
                                @ColumnResult(name = "day", type = Integer.class),
                                @ColumnResult(name = "month", type = Integer.class),
                                @ColumnResult(name = "year", type = Integer.class),
                                @ColumnResult(name = "totalRelease", type = Double.class),
                                @ColumnResult(name = "totalRefund", type = Double.class),
                                @ColumnResult(name = "expiredCreditsPercent", type = Double.class),
                                @ColumnResult(name = "totalProfit", type = Double.class),
                                @ColumnResult(name = "profitPlus", type = Double.class)
                        }
                )
        }
)
public class DayStatistic {

    @Id
    @GeneratedValue
    private int id;

    @Column(nullable = false)
    @NonNull
    private LocalDate date;

    @Column(nullable = false)
    @NonNull
    private Double cashInflow;

    @Column(nullable = false)
    @NonNull
    private Double profitMargin;

    @OneToOne
    @NonNull
    private Credit credit;
}
