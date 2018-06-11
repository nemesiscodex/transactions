package io.nemesiscodex.transactions.dao;

import io.nemesiscodex.transactions.dto.StatisticDto;
import io.nemesiscodex.transactions.dto.TransactionDto;

public interface TransactionDao {
    boolean saveTransaction(TransactionDto transaction);

    StatisticDto getLast60SecondsTransactions();
}
