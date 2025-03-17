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

    public UserConnection addConnection(User user, User friend) {
        UserConnection connection = new UserConnection();
        connection.setUser(user);
        connection.setFriend(friend);
        return userConnectionRepository.save(connection);
    }

    public List<UserConnection> getConnections(int userId) {
        return userConnectionRepository.findByUserId(userId);
    }
}