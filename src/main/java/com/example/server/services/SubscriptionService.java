package com.example.server.services;

import com.example.server.models.Account;
import com.example.server.models.Subscription;
import com.example.server.repositories.AccountRepository;
import com.example.server.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VisitRepository visitRepository;

    public List<Subscription> updateSubscriptions(Account account) {
        List<Subscription> subscriptions = account.getSubscriptions();
        int lastIndex = subscriptions.size() - 1;
        if (!subscriptions.isEmpty()) {
            int visitsSize = visitRepository.findBySubscription(subscriptions.get(lastIndex)).size();
            subscriptions.get(lastIndex).setVisitCounter(visitsSize);
            account.setSubscriptions(subscriptions);
            accountRepository.save(account);
            return subscriptions;
        }
        return Collections.EMPTY_LIST;
    }
}
