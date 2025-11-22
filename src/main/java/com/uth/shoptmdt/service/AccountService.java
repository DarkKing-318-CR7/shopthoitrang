package com.uth.shoptmdt.service;
// src/main/java/.../service/AccountService.java

import com.uth.shoptmdt.service.dto.PasswordForm;
import com.uth.shoptmdt.service.dto.ProfileForm;
import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateProfile(User user, ProfileForm f) {
        user.setFullName(f.getFullName());
        user.setEmail(f.getEmail());
        user.setPhone(f.getPhone());
        user.setAddressLine(f.getAddressLine());
        user.setProvince(f.getProvince());
        user.setDistrict(f.getDistrict());
        user.setWard(f.getWard());
        userRepository.save(user);
    }

    @Transactional
    public void changePassword(User user, PasswordForm f) {
        if (!passwordEncoder.matches(f.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không đúng");
        }
        if (!f.getNewPassword().equals(f.getConfirmPassword())) {
            throw new IllegalArgumentException("Xác nhận mật khẩu không khớp");
        }
        if (passwordEncoder.matches(f.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        user.setPassword(passwordEncoder.encode(f.getNewPassword()));
        userRepository.save(user);
    }
}
