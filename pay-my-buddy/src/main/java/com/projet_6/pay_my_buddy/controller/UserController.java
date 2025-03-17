package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@ModelAttribute User user) {
        User createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }


    @PostMapping("/update-balance/{id}")
    public ResponseEntity<User> updateBalance(@PathVariable int id, @RequestParam Double amount) {
        User updatedUser = userService.updateBalance(id, amount);
        return ResponseEntity.ok(updatedUser);
    }

}

