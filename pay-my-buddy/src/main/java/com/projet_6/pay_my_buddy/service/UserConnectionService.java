package com.projet_6.pay_my_buddy.service;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.repository.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserConnectionService {
    @Autowired
    private UserConnectionRepository userConnectionRepository;

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

    public List<UserConnection> getConnections(User user) {
        return userConnectionRepository.findByUserId(user.getId());
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
}

