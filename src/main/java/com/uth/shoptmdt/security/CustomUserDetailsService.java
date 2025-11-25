package com.uth.shoptmdt.security;

import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Không tìm thấy user: " + username));

        // ===== XỬ LÝ ROLE NULL AN TOÀN =====
        String roleName = "ROLE_USER";  // default

        if (user.getRole() != null
                && user.getRole().getName() != null
                && !user.getRole().getName().isBlank()) {
            roleName = user.getRole().getName();
        }

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(roleName));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),   // enabled
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                true,               // accountNonLocked
                authorities
        );
    }
}
