package com.example.server.repositories;

import com.example.server.models.Account;
import com.example.server.models.Subscription;
import com.example.server.models.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitRepository  extends JpaRepository<Visit, Long> {
    List<Visit> findByAccount(Optional<Account> account);
    List<Visit> findBySubscription(Subscription subscription);
}
