package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Subscription;
import com.example.server.models.Visit;
import com.example.server.repositories.AccountRepository;
import com.example.server.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/visit")
public class VisitController {

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/getByAccount/{id}", method = RequestMethod.GET)
    public List<Visit> getVisitsByAccount(@PathVariable("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        List<Visit> visits = visitRepository.findByAccount(account);
        visits.sort(Comparator.comparing(Visit::getDate));
        return visits;
    }

    @RequestMapping(value = "/getBySubscription/{id}", method = RequestMethod.GET)
    public List<Visit> getVisitsBySubscription(@PathVariable("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        int listSize = account.get().getSubscriptions().size();
        List<Visit> visits = Collections.EMPTY_LIST;
        if (listSize != 0) {
            List<Subscription> subscriptions = account.get().getSubscriptions();
            visits = visitRepository.findBySubscription(subscriptions.get(listSize-1));
            visits.sort(Comparator.comparing(Visit::getDate));
        }
        return visits;
    }
}
