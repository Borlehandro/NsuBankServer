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
                   select (case
                               when (day is not null) then day::::text
                               when (month is not null) then date_part('year', month) || '-' || date_part('month', month)
                               when (year is not null) then date_part('year', year)::::text
                       end) as period,
                          release,
                          refund,
                          expired_percent,
                          margin
                   from (
                            select coalesce(release.sum, 0)                                           as release,
                                   coalesce(refund.sum, 0)                                            as refund,
                                   coalesce(expired.expired_percent, 0)                               as expired_percent,
                                   coalesce(profit.margin, 0)                                         as margin,
                                   coalesce(release.day, refund.day, expired.day, profit.day)         as day,
                                   coalesce(release.month, refund.month, expired.month, profit.month) as month,
                                   coalesce(release.year, refund.year, expired.year, profit.year)     as year
                            from aggregated_release release
                                     left join aggregated_refund refund
                                               on release.day is not distinct from refund.day and
                                                  release.month is not distinct from refund.month and
                                                  release.year is not distinct from refund.year
                                     left join expired_percent_view expired
                                               on release.day is not distinct from expired.day and
                                                  release.month is not distinct from expired.month and
                                                  release.year is not distinct from expired.year
                                     left join profit_view profit
                                               on release.day is not distinct from profit.day and
                                                  release.month is not distinct from profit.month and
                                                  release.year is not distinct from profit.year
                            union all
                            select coalesce(release.sum, 0)                                           as release,
                                   coalesce(refund.sum, 0)                                            as refund,
                                   coalesce(expired.expired_percent, 0)                               as expired_percent,
                                   coalesce(profit.margin, 0)                                         as margin,
                                   coalesce(release.day, refund.day, expired.day, profit.day)         as day,
                                   coalesce(release.month, refund.month, expired.month, profit.month) as month,
                                   coalesce(release.year, refund.year, expired.year, profit.year)     as year
                            from aggregated_refund refund
                                     left join aggregated_release release
                                               on release.day is not distinct from refund.day and
                                                  release.month is not distinct from refund.month and
                                                  release.year is not distinct from refund.year
                                     left join expired_percent_view expired
                                               on refund.day is not distinct from expired.day and
                                                  refund.month is not distinct from expired.month and
                                                  refund.year is not distinct from expired.year
                                     left join profit_view profit
                                               on refund.day is not distinct from profit.day and
                                                  refund.month is not distinct from profit.month and
                                                  refund.year is not distinct from profit.year
                            where release.sum is null
                            union all
                            select coalesce(release.sum, 0)                                           as release,
                                   coalesce(refund.sum, 0)                                            as refund,
                                   coalesce(expired.expired_percent, 0)                               as expired_percent,
                                   coalesce(profit.margin, 0)                                         as margin,
                                   coalesce(release.day, refund.day, expired.day, profit.day)         as day,
                                   coalesce(release.month, refund.month, expired.month, profit.month) as month,
                                   coalesce(release.year, refund.year, expired.year, profit.year)     as year
                            from expired_percent_view expired
                                     left join aggregated_release release
                                               on release.day is not distinct from expired.day and
                                                  release.month is not distinct from expired.month and
                                                  release.year is not distinct from expired.year
                                     left join aggregated_refund refund
                                               on refund.day is not distinct from expired.day and
                                                  refund.month is not distinct from expired.month and
                                                  refund.year is not distinct from expired.year
                                     left join profit_view profit
                                               on expired.day is not distinct from profit.day and
                                                  expired.month is not distinct from profit.month and
                                                  expired.year is not distinct from profit.year
                            where release.sum is null
                              and refund.sum is null
                            union all
                            select coalesce(release.sum, 0)                                           as release,
                                   coalesce(refund.sum, 0)                                            as refund,
                                   coalesce(expired.expired_percent, 0)                               as expired_percent,
                                   coalesce(profit.margin, 0)                                         as margin,
                                   coalesce(release.day, refund.day, expired.day, profit.day)         as day,
                                   coalesce(release.month, refund.month, expired.month, profit.month) as month,
                                   coalesce(release.year, refund.year, expired.year, profit.year)     as year
                            from profit_view profit
                                     left join aggregated_release release
                                               on release.day is not distinct from profit.day and
                                                  release.month is not distinct from profit.month and
                                                  release.year is not distinct from profit.year
                                     left join aggregated_refund refund
                                               on refund.day is not distinct from profit.day and
                                                  refund.month is not distinct from profit.month and
                                                  refund.year is not distinct from profit.year
                                     left join expired_percent_view expired
                                               on expired.day is not distinct from profit.day and
                                                  expired.month is not distinct from profit.month and
                                                  expired.year is not distinct from profit.year
                            where release.sum is null
                              and refund.sum is null
                              and expired.expired_percent is null
                        ) sub
                   order by coalesce(day, month, year);
                   """, resultSetMapping = "ReportRecord")

@SqlResultSetMapping(
        name = "ReportRecord",
        classes = {
                @ConstructorResult(
                        targetClass = ReportRecord.class,
                        columns = {
                                @ColumnResult(name = "period", type = String.class),
                                @ColumnResult(name = "release", type = Double.class),
                                @ColumnResult(name = "refund", type = Double.class),
                                @ColumnResult(name = "expired_percent", type = Double.class),
                                @ColumnResult(name = "margin", type = Double.class),
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
