package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.Role;
import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.RoleRepository;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public List<User> findAll() {
        return repo.findAll();
    }


    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }


    public User save(User user) {

        // ===== PASSWORD ENCODE =====
        if (user.getId() == null) { // tạo mới
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else { // update
            // nếu không bắt đầu bằng prefix bcrypt => encode lại
            String pw = user.getPassword();
            if (pw != null && !pw.isBlank() && !pw.startsWith("$2a$") && !pw.startsWith("$2b$")) {
                user.setPassword(passwordEncoder.encode(pw));
            }
        }

        // ===== GÁN ROLE MẶC ĐỊNH NẾU BỊ NULL =====
        if (user.getRole() == null) {
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy role ROLE_USER"));
            user.setRole(defaultRole);
        }



        return repo.save(user);
    }


    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
