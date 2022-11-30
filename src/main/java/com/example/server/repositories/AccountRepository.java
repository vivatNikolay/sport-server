package com.example.server.repositories;

import com.example.server.models.Account;
import com.example.server.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
    Account findByEmail(String email);

    List<Account> findAllByRole(Role role);
}
