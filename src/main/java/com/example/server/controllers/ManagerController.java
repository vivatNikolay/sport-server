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

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @RequestMapping(value = "/sportsmen", method = RequestMethod.GET)
    public Set<Account> getSportsmenByQuery(@RequestParam("query") String query, HttpServletResponse httpServletResponse) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        String[] words = query.toLowerCase().split(" ", 3);
        List<Account> allUsers = accountRepository.findAllByRole(Role.USER);
        Set<Account> resultList = new HashSet<Account>(Collections.EMPTY_SET);
        for (String word : words) {
            resultList.addAll(allUsers.stream().filter((user) ->
                    user.getFirstName().toLowerCase().contains(word) || user.getLastName().toLowerCase().contains(word)
            ).collect(Collectors.toSet()));
        }
        return resultList;
    }

    @RequestMapping(value = "/sportsman", method = RequestMethod.GET)
    public ResponseEntity<Account> getSportsmanByEmail(@RequestParam("email") String email, HttpServletResponse httpServletResponse) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        Account account = accountRepository.findByEmail(email);
        if (Role.USER.equals(account.getRole())) {
            return ResponseEntity.ok(account);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/addSingleVisit", method = RequestMethod.POST)
    public ResponseEntity<String> addSingleVisit(@RequestParam("email") String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && Role.USER.equals(account.getRole())) {
            subscriptionService.addSingleVisit(account);
            return ResponseEntity.ok(account.getEmail());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/addVisitToMembership", method = RequestMethod.POST)
    public ResponseEntity<String> addVisitToMembership(@RequestParam("email") String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null && Role.USER.equals(account.getRole())) {
            boolean isAdded = subscriptionService.addVisitToMembership(account);
            return isAdded ? ResponseEntity.ok(account.getEmail()) : new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/addMembership", method = RequestMethod.POST)
    public ResponseEntity<String> addMembership(@RequestParam("email") String email,
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
            return ResponseEntity.ok(account.getEmail());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> createSportsman(@RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(newAccount.getEmail());
        if (account != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        newAccount.setRole(Role.USER);
        accountRepository.save(newAccount);
        return ResponseEntity.ok(newAccount.getEmail());
    }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    public String updateAccountByEmail(@RequestBody Account newAccount) {
        Account account = accountRepository.findByEmail(newAccount.getEmail());
        account.setPassword(newAccount.getPassword());
        account.setPhone(newAccount.getPhone());
        account.setFirstName(newAccount.getFirstName());
        account.setLastName(newAccount.getLastName());
        account.setGender(newAccount.getGender());
        account.setIconNum(newAccount.getIconNum());
        account.setDateOfBirth(newAccount.getDateOfBirth());
        accountRepository.save(account);
        return account.getEmail();
    }
}
