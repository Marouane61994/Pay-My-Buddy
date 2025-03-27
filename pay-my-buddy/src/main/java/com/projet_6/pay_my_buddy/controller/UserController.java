package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/users/login";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        // Suppose that user is retrieved from session/auth
        User user = userService.findByEmail("test@example.com").orElse(new User());
        model.addAttribute("user", user);
        return "profile";
    }

}

