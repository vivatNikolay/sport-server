package com.example.server.controllers;

import com.example.server.models.Account;
import com.example.server.models.Role;
import com.example.server.models.Subscription;
import com.example.server.models.Visit;
import com.example.server.repositories.AccountRepository;
import com.example.server.repositories.VisitRepository;
import com.example.server.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sportsmanDetails")
public class SportsmanDetailsController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VisitRepository visitRepository;

    @RequestMapping(value = "/visits/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<Visit>> getVisits(@PathVariable("email") String email, Principal principal,
                                                 HttpServletResponse httpServletResponse) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        Account account = accountRepository.findByEmail(email);
        if (checkAccess(principal, account)) {
            List<Visit> visits = visitRepository.findByAccount(account);
            visits.sort(Comparator.comparing(Visit::getDate));
            return ResponseEntity.ok(visits);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    private boolean checkAccess(Principal principal, Account account) {
        Account authAcc = accountRepository.findByEmail(principal.getName());
        if (authAcc.getRole().equals(Role.ADMIN) || authAcc.getRole().equals(Role.MANAGER)) {
            return true;
        }
        return authAcc.equals(account);
    }
}
