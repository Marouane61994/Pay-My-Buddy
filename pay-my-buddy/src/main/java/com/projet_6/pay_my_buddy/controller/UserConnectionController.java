package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<UserConnection> addConnection(@RequestParam int userId, @RequestParam int friendId) {
        User user = userService.getUserById(userId).orElseThrow();
        User friend = userService.getUserById(friendId).orElseThrow();
        UserConnection connection = userConnectionService.addConnection(user, friend);
        return ResponseEntity.ok(connection);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserConnection>> getConnections(@PathVariable int userId) {
        List<UserConnection> connections = userConnectionService.getConnections(userId);
        return ResponseEntity.ok(connections);
    }
}
