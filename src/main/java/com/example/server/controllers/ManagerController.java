package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.repositories.AccountRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/allSportsmen", method = RequestMethod.GET)
    public List<Account> getAllSportsmen() {
        return accountRepository.findAllByRole(Role.USER);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> getSportsmanById(@PathVariable("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent() && account.get().getRole().equals(Role.USER)) {
            subscriptionService.updateSubscriptions(account.get());
            return ResponseEntity.ok(account.get());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Long> createSportsman(@RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(newAccount.getEmail());
        if (account != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newAccount.setRole(Role.USER);
        accountRepository.save(newAccount);
        return ResponseEntity.ok(newAccount.getId());
    }
}
