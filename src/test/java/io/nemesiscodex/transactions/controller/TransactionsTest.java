package io.nemesiscodex.transactions.controller;

import io.nemesiscodex.transactions.dao.InMemoryTransactionDao;
import io.nemesiscodex.transactions.dao.TransactionDao;
import io.nemesiscodex.transactions.dto.StatisticDto;
import io.nemesiscodex.transactions.dto.TransactionDto;
import io.nemesiscodex.transactions.service.BasicStatisticService;
import io.nemesiscodex.transactions.service.BasicTransactionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.*;

public class TransactionsTest {

    @Test
    public void createTransaction() {
        TransactionDao dao = new InMemoryTransactionDao();

        BasicTransactionService transactionService = new BasicTransactionService(dao);
        BasicStatisticService statisticService = new BasicStatisticService(dao);

        Transactions transactions = new Transactions(transactionService, statisticService);

        ResponseEntity response;

        // Current time
        response = transactions.createTransaction(new TransactionDto(10.5, new Date()));
        Assert.assertEquals("Valid transaction should return 201",
                201, response.getStatusCode().value());

        // Time in the future
        response = transactions.createTransaction(
                new TransactionDto(10.5,
                        Date.from(Instant.now().plus(2, ChronoUnit.SECONDS))));
        Assert.assertEquals("Invalid transaction should return 204",
                204, response.getStatusCode().value());
    }

    @Test
    public void getStatistics() {
        TransactionDao dao = new InMemoryTransactionDao();

        BasicTransactionService transactionService = new BasicTransactionService(dao);
        BasicStatisticService statisticService = new BasicStatisticService(dao);

        Transactions transactions = new Transactions(transactionService, statisticService);

        ResponseEntity<StatisticDto>  response;

        // no transactions
        response = transactions.getStatistics();
        Assert.assertEquals("Status should be 200", 200, response.getStatusCode().value());
        Assert.assertEquals("Count should be 0", 0, response.getBody().getCount());

        // after first transaction
        transactions.createTransaction(new TransactionDto(10.5, new Date()));
        response = transactions.getStatistics();
        Assert.assertEquals("Status should be 200", 200, response.getStatusCode().value());
        Assert.assertEquals("Count should be 1", 1, response.getBody().getCount());
        Assert.assertEquals("Sum should be 10.5", 10.5, response.getBody().getSum(), 0.001);

    }
}