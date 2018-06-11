package io.nemesiscodex.transactions.dao;

import io.nemesiscodex.transactions.dto.StatisticDto;
import io.nemesiscodex.transactions.dto.TransactionDto;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class InMemoryTransactionDaoTest {

    @Test
    public void saveTransaction() {
        TransactionDao dao = new InMemoryTransactionDao();

        // insert with current timestamp
        Assert.assertTrue("Transaction with current time should be saved",
                dao.saveTransaction(new TransactionDto(10, Date.from(Instant.now()))));

        // insert in the future
        Assert.assertFalse("Transaction in the future should not be saved",
                dao.saveTransaction(new TransactionDto(10,
                Date.from(Instant.now().plus(3, ChronoUnit.SECONDS)))));

        // insert 10 seconds in the pass
        Assert.assertTrue("Transaction from 10 seconds ago should be saved",
                dao.saveTransaction(new TransactionDto(10,
                Date.from(Instant.now().minus(10, ChronoUnit.SECONDS)))));

        // insert 65 seconds in the pass
        Assert.assertFalse("Transaction from 65 seconds ago should not be saved",
                dao.saveTransaction(new TransactionDto(10,
                Date.from(Instant.now().minus(65, ChronoUnit.SECONDS)))));

    }

    @Test
    public void getLast60SecondsTransactions() {
        TransactionDao dao = new InMemoryTransactionDao();
        StatisticDto statistics;
        // No transactions in the last 60 seconds
        statistics = dao.getLast60SecondsTransactions();
        verifyStatistic(statistics, 0,0,0,0,0);

        // single transaction
        dao.saveTransaction(new TransactionDto(10, Date.from(Instant.now())));
        statistics = dao.getLast60SecondsTransactions();
        verifyStatistic(statistics, 1,10,10,10,10);


        // more than one transaction at the same time
        Date time = Date.from(Instant.now().minus(10, ChronoUnit.SECONDS));
        dao.saveTransaction(new TransactionDto(30, time));
        dao.saveTransaction(new TransactionDto(-20, time));
        statistics = dao.getLast60SecondsTransactions();
        verifyStatistic(statistics, 3,20,20/3.0,-20,30);

        // a transaction out of the range should not alter the statistic
        time = Date.from(Instant.now().minus(65, ChronoUnit.SECONDS));
        dao.saveTransaction(new TransactionDto(50, time));
        statistics = dao.getLast60SecondsTransactions();
        verifyStatistic(statistics, 3,20,20/3.0,-20,30);

        time = Date.from(Instant.now().plus(5, ChronoUnit.SECONDS));
        dao.saveTransaction(new TransactionDto(50, time));
        statistics = dao.getLast60SecondsTransactions();
        verifyStatistic(statistics, 3,20,20/3.0,-20,30);


    }

    private void verifyStatistic(StatisticDto statistics, long count, double sum, double avg, double min, double max) {
        Assert.assertEquals(String.format("Count should be %d", count), count, statistics.getCount());
        Assert.assertEquals(String.format("Sum should be %f", sum), sum, statistics.getSum(), 0.001);
        Assert.assertEquals(String.format("Avg should be %f", avg), avg, statistics.getAvg(), 0.001);
        Assert.assertEquals(String.format("Min should be %f", min), min, statistics.getMin(), 0.001);
        Assert.assertEquals(String.format("Max should be %f", max), max, statistics.getMax(), 0.001);
    }
}