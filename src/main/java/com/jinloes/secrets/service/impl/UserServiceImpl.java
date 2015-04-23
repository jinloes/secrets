package com.jinloes.secrets.service.impl;

import com.jinloes.secrets.repositories.api.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService}.
 */
@Service
public class UserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired private ApplicationContext applicationContext;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Email = username in this context
        return userRepository.findByEmail(username);
    }
}
