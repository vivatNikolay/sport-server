package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.models.Subscription;
import com.example.server.repositories.AccountRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<Long> addVisitToSportsman(@RequestParam("email") String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && Role.USER.equals(account.getRole())) {
            subscriptionService.addVisit(account);
            return ResponseEntity.ok(account.getId());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/addMembership", method = RequestMethod.POST)
    public ResponseEntity<Long> addMembership(@RequestParam("email") String email,
                                                @RequestParam("dateOfStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfPurchase,
                                                @RequestParam("dateOfEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfEnd,
                                                @RequestParam("numberOfVisits") int numberOfVisits) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && Role.USER.equals(account.getRole())) {
            Subscription newSub = new Subscription();
            newSub.setDateOfStart(dateOfPurchase);
            newSub.setDateOfEnd(dateOfEnd);
            newSub.setNumberOfVisits(numberOfVisits);
            List<Subscription> subscriptions = account.getSubscriptions();
            subscriptions.add(newSub);
            account.setSubscriptions(subscriptions);
            accountRepository.save(account);
            return ResponseEntity.ok(account.getId());
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

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public Long updateAccountByEmail(@RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(newAccount.getEmail());
        account.setPassword(newAccount.getPassword());
        account.setPhone(newAccount.getPhone());
        account.setFirstName(newAccount.getFirstName());
        account.setGender(newAccount.getGender());
        account.setIconNum(newAccount.getIconNum());
        accountRepository.save(account);
        return account.getId();
    }
}
