package com.uth.shoptmdt.security;

import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String roleName = (u.getRole() != null && u.getRole().getName() != null)
                ? u.getRole().getName().trim()
                : "USER";
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(authorities)
                .accountLocked(Boolean.FALSE.equals(u.isEnabled()))
                .disabled(Boolean.FALSE.equals(u.isEnabled()))
                .build();
    }
}
