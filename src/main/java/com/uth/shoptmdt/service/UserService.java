package com.uth.shoptmdt.service;

import com.uth.shoptmdt.entity.User;
import com.uth.shoptmdt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository repo;


    public List<User> findAll() {
        return repo.findAll();
    }


    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }


    public User save(User user) {
        return repo.save(user);
    }


    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
