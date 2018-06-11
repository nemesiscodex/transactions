package io.nemesiscodex.transactions.service;

import io.nemesiscodex.transactions.dao.TransactionDao;
import io.nemesiscodex.transactions.dto.StatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasicStatisticService implements StatisticService {

    private final TransactionDao dao;

    @Autowired
    public BasicStatisticService(TransactionDao dao) {
        this.dao = dao;
    }

    @Override
    public StatisticDto getStatistics() {
        return dao.getLast60SecondsTransactions();
    }

}
