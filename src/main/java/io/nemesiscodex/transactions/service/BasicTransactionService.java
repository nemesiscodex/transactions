package io.nemesiscodex.transactions.service;

import io.nemesiscodex.transactions.dao.TransactionDao;
import io.nemesiscodex.transactions.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicTransactionService implements TransactionService {

    private final TransactionDao dao;

    @Autowired
    public BasicTransactionService(TransactionDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean saveTransaction(TransactionDto transaction) {
        return dao.saveTransaction(transaction);
    }
}
