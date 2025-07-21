package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import com.app.entity.Account;

import java.util.List;

@Service
public class AccountRegistrationService {
    
    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;
    
    @Autowired
    private AuthorityService authorityService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void registerNewAccount(Account account) {
        List<String> roles = authorityService.findRoles(account.getUsername());
        String[] r = new String[roles.size()];
        roles.toArray(r);

        UserDetails user = User.withUsername(account.getUsername())
                .password(passwordEncoder.encode(account.getPassword()))
                .roles(r).build();

        userDetailsManager.createUser(user);
    }

    public void updateAccount(Account account) {
        UserDetails existingUser = userDetailsManager.loadUserByUsername(account.getUsername());
        UserDetails updatedUser = User.withUserDetails(existingUser)
                .password(passwordEncoder.encode(account.getPassword()))
                .build();

        userDetailsManager.updateUser(updatedUser);
    }
}
