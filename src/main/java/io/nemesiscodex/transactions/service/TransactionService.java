package io.nemesiscodex.transactions.service;

import io.nemesiscodex.transactions.dto.TransactionDto;

public interface TransactionService {
    boolean saveTransaction(TransactionDto transaction);
}
