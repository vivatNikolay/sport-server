package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.repositories.AccountRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/sportsmen", method = RequestMethod.GET)
    public List<Account> getSportsmenByQuery(@RequestParam("query") String query) {
        return accountRepository.findAllByRole(Role.USER)
                .stream().filter(acc -> acc.getFirstName().toLowerCase().contains(query)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/sportsman", method = RequestMethod.GET)
    public ResponseEntity<Account> getSportsmanByEmail(@RequestParam("email") String email) {
        Account account = accountRepository.findByEmail(email);
        if (Role.USER.equals(account.getRole())) {
            return ResponseEntity.ok(account);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/addVisit", method = RequestMethod.POST)
    public ResponseEntity<Long> addVisitToSportsman(@RequestParam("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent() && Role.USER.equals(account.get().getRole())) {
            subscriptionService.addVisit(account.get());
            return ResponseEntity.ok(account.get().getId());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> getSportsmanById(@PathVariable("id") Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent() && account.get().getRole().equals(Role.USER)) {
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
