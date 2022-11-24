package com.example.server.config;

import javax.inject.Inject;

import com.example.server.models.Account;
import com.example.server.repositories.AccountRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Inject
    private AccountRepository accountRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/api/**").authenticated().and().httpBasic();
        //здесь настроить разные урлы на разные роли
    }

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Transactional(readOnly = true)
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Account account = accountRepository.findByEmail(email);
                if (account == null) {
                    throw new UsernameNotFoundException(email);
                }
                return User.withUsername(account.getEmail())
                        .password(account.getPassword())
                        .disabled(false)
                        .authorities("USER")
                        .build();
            }

        };
    }

}