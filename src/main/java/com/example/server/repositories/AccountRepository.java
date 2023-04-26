package com.example.server.repositories;

import com.example.server.models.Account;
import com.example.server.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findById(String id);
    Account findByEmail(String email);

    List<Account> findAllByRole(Role role);
}
