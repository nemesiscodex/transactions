package io.nemesiscodex.transactions.controller;


import io.nemesiscodex.transactions.dto.StatisticDto;
import io.nemesiscodex.transactions.dto.TransactionDto;
import io.nemesiscodex.transactions.service.StatisticService;
import io.nemesiscodex.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
public class Transactions {

    private final TransactionService transactionService;

    private final StatisticService statisticService;

    @Autowired
    public Transactions(TransactionService transactionService, StatisticService statisticService) {
        this.transactionService = transactionService;
        this.statisticService = statisticService;
    }

    @PostMapping("/transactions")
    public ResponseEntity createTransaction(@RequestBody TransactionDto transaction) {
        if (transactionService.saveTransaction(transaction)) {
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticDto> getStatistics() {
        return ResponseEntity.ok(statisticService.getStatistics());
    }

}
