package com.projet_6.pay_my_buddy.service;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public User registerUser(User user) {
        user.setBalance(0.0);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateBalance(int userId, Double amount) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setBalance(user.getBalance() + amount);
        return userRepository.save(user);
    }

}

