package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.repositories.AccountRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Long updateAccountByEmail(Principal principal, @RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(principal.getName());
        account.setPassword(newAccount.getPassword());
        account.setPhone(newAccount.getPhone());
        account.setFirstName(newAccount.getFirstName());
        account.setGender(newAccount.getGender());
        account.setIconNum(newAccount.getIconNum());
        accountRepository.save(account);
        return account.getId();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccountByEmail(Principal principal) {
        Account account = accountRepository.findByEmail(principal.getName());
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
