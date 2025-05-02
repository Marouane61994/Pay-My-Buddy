package com.projet_6.pay_my_buddy.service;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class UserConnectionService {
    @Autowired
    private UserConnectionRepository userConnectionRepository;

    @Autowired
    private UserService userService;


    public void addConnection(User user, User friend) {
        boolean alreadyConnected = userConnectionRepository.findByUserId(user.getId())
                .stream()
                .anyMatch(conn -> conn.getFriend().getId() == friend.getId());

        if (alreadyConnected) {
            throw new IllegalStateException("Cet utilisateur est déjà votre ami.");
        }

        UserConnection connection = new UserConnection();
        connection.setUser(user);
        connection.setFriend(friend);
        userConnectionRepository.save(connection);
    }

    public boolean existsConnection(User user, User friend) {
        return userConnectionRepository.existsByUserAndFriend(user, friend);
    }

    public List<User> getFriendsForUser(User user) {
        return userConnectionRepository.findByUserId(user.getId())
                .stream()
                .map(UserConnection::getFriend)
                .toList();
    }

    public void addRelation(User loggedUser, String friendEmail) {
        Optional<User> friendOptional = userService.findByEmail(friendEmail);

        if (friendOptional.isEmpty()) {
            throw new RuntimeException("Aucun utilisateur avec cet email.");
        }
        User friend = friendOptional.get();
        boolean alreadyConnected = existsConnection(loggedUser, friend);
        if (alreadyConnected) {
            throw new RuntimeException("Vous êtes déjà connecté à cet utilisateur.");

        }
        addConnection(loggedUser, friend);
    }
}

