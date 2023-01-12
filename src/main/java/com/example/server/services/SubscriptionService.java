package com.example.server.services;

import com.example.server.models.Account;
import com.example.server.models.Subscription;
import com.example.server.models.Visit;
import com.example.server.repositories.AccountRepository;
import com.example.server.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VisitRepository visitRepository;

    public void addVisit(Account account) {
        List<Subscription> subscriptions = account.getSubscriptions();
        Visit newVisit = new Visit();
        newVisit.setDate(Date.valueOf(LocalDate.now()));
        newVisit.setAccount(account);
        if (!subscriptions.isEmpty()) {
            int lastIndex = subscriptions.size() - 1;
            boolean isActive = (!subscriptions.get(lastIndex).getDateOfEnd().before(Date.valueOf(LocalDate.now())) ||
                    !subscriptions.get(lastIndex).getDateOfStart().after(Date.valueOf(LocalDate.now()))) &&
                    subscriptions.get(lastIndex).getNumberOfVisits() > subscriptions.get(lastIndex).getVisits().size();
            if (isActive) {
                subscriptions.get(lastIndex).getVisits().add(newVisit);
                account.setSubscriptions(subscriptions);
                accountRepository.save(account);
                return;
            }
        }
        visitRepository.save(newVisit);
    }
}
