package com.projet_6.pay_my_buddy.service;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


//TODO : encrypter le mot de passe

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User saveUser(User user) {
        return userRepository.save(user);
    }

    //Methode qui remplace l'authentification
    public User getCurrentUser() {
        User loggedUser = getAllUsers().get(0);
        return loggedUser;
    }

}

