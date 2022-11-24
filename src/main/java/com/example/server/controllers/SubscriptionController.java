package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Subscription;
import com.example.server.repositories.AccountRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscription")
public class SubscriptionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public List<Subscription> getByAccount(@PathVariable("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return subscriptionService.updateSubscriptions(account.get());
    }
}
