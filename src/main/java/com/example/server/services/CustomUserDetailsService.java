package com.example.server.services;

import com.example.server.models.Account;
import com.example.server.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException(email);
        }
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        return User.withUsername(account.getEmail())
                .password(bCrypt.encode(account.getPassword()))
                .disabled(false)
                .authorities(account.getRole().name())
                .build();
    }
}