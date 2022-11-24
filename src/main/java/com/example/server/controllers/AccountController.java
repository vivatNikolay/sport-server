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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sportsman")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Long createSportsman(@RequestBody Account newAccount) {
        Account account = new Account();
        account.setEmail("dasha@gmail.com");
        account.setPassword("1111");
        account.setPhone("+375291232124");
        account.setFirstName("Darya");
        account.setDateOfBirth(new Date(1998, 8, 23));
        account.setGender(true);
        account.setIconNum(account.getGender() ? 1 : 2);
        account.setRole(Role.SPORTSMAN);
        accountRepository.save(account);
        return account.getId();
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.PUT)
    public Long updateAccountByEmail(@PathVariable("email") String email, @RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(email);
        account.setPassword(newAccount.getPassword());
        account.setPhone(newAccount.getPhone());
        account.setFirstName(newAccount.getFirstName());
        account.setGender(newAccount.getGender());
        account.setIconNum(newAccount.getIconNum());
        accountRepository.save(account);
        return account.getId();
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccountByEmail(@PathVariable("email") String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            subscriptionService.updateSubscriptions(account);
            return ResponseEntity.ok(account);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/{email}", method = RequestMethod.DELETE)
    public void deleteAccount(@PathVariable("email") String email) {
        accountRepository.delete(accountRepository.findByEmail(email));
    }
}
