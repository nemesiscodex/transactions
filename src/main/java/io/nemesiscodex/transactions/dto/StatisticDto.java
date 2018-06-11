package io.nemesiscodex.transactions.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticDto {
    private final double sum;
    private final double avg;
    private final double max;
    private final double min;
    private final long count;

    public static StatisticDto empty() {
        return StatisticDto.builder()
                .sum(0)
                .count(0)
                .avg(0)
                .min(0)
                .max(0)
                .build();
    }

    public static StatisticDto fromTransaction(TransactionDto transaction) {
        double amount = transaction.getAmount();
        return StatisticDto.builder()
                .sum(amount)
                .avg(amount)
                .count(1)
                .min(amount)
                .max(amount)
                .build();
    }

    public StatisticDto update(TransactionDto transaction) {
        double amount = transaction.getAmount();

        return StatisticDto.builder()
                .sum(sum + amount)
                .count(count + 1)
                .avg((sum + amount) / (count + 1))
                .max(Math.max(amount, count == 0? Double.MIN_VALUE: max))
                .min(Math.min(amount, count == 0? Double.MAX_VALUE: min))
                .build();
    }

    public StatisticDto update(StatisticDto statistic) {
        return StatisticDto.builder()
                .sum(statistic.getSum() + sum)
                .count(statistic.getCount() + count)
                .avg((statistic.getSum() + sum) / (statistic.getCount() + count))
                .max(Math.max(statistic.getMax(), count == 0? Double.MIN_VALUE: max))
                .min(Math.min(statistic.getMin(), count == 0? Double.MAX_VALUE: min))
                .build();
    }
}
