package com.bank.authorization.Security;

import com.bank.authorization.Entities.User;
import com.bank.authorization.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Long profileId;
        try {
            profileId = Long.parseLong(username);
        } catch (NumberFormatException ex) {
            throw new UsernameNotFoundException("Invalid profileId format: " + username);
        }
        final User user = userRepository.findByProfileId(profileId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with profileId: " + username));
        final List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole())
        );
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getProfileId()),
                user.getPassword(),
                authorities
        );
    }
}
