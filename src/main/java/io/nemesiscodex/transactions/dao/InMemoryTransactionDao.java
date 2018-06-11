package io.nemesiscodex.transactions.dao;

import io.nemesiscodex.transactions.dto.StatisticDto;
import io.nemesiscodex.transactions.dto.TransactionDto;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryTransactionDao implements TransactionDao {

    // Spring components are singletons by default
    // Otherwise store and lock should be static
    private final Object lock = new Object();
    private volatile Map<Long, StatisticDto> store = new ConcurrentHashMap<>();
    private static final int MILLIS = 1000;
    private static final int ONE_MINUTE = 60;

    @Override
    public boolean saveTransaction(TransactionDto transaction) {
        synchronized (lock) {
            long seconds = transaction.getTimestamp().getTime() / MILLIS;
            long now = new Date().getTime() / MILLIS;
            long diff = now - seconds;
            if (diff > ONE_MINUTE || diff < 0) {
                return false;
            }
            Map<Long, StatisticDto> newStore = new ConcurrentHashMap<>();
            for (long time = now; time >= now - ONE_MINUTE; time--) {
                if (store.containsKey(time)) {
                    newStore.put(time, store.get(time));
                }
            }

            if (newStore.containsKey(seconds)) {
                newStore.put(seconds, newStore.get(seconds).update(transaction));
            } else {
                newStore.put(seconds, StatisticDto.fromTransaction(transaction));
            }

            store = newStore;
        }
        return true;
    }

    @Override
    public StatisticDto getLast60SecondsTransactions() {
        Map<Long, StatisticDto> currentStore = store;
        long now = new Date().getTime() / MILLIS;

        StatisticDto ret = StatisticDto.empty();
        for (long time = now; time > now - ONE_MINUTE; time--) {
            if (currentStore.containsKey(time)) {
                ret = ret.update(currentStore.get(time));
            }
        }
        return ret;
    }
}
