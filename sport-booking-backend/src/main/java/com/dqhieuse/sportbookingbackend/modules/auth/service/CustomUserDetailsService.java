package com.dqhieuse.sportbookingbackend.modules.auth.service;

import com.dqhieuse.sportbookingbackend.common.exception.AppException;
import com.dqhieuse.sportbookingbackend.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(HttpStatus.UNAUTHORIZED, "User not found with username: " + username)
        );
    }
}
