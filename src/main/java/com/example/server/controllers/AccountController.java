package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String updateAccountByEmail(Principal principal, @RequestBody Account newAccount) {
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
    public ResponseEntity<Account> getAccountByEmail(Principal principal, HttpServletResponse httpServletResponse) {
        Account account = accountRepository.findByEmail(principal.getName());
        httpServletResponse.setCharacterEncoding("UTF-8");
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
