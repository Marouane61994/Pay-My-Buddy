package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Cet email est déjà utilisé !");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", loggedUser);
        return "editprofile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", loggedUser);
        return "editprofile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User updatedUser,
                                Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }
        loggedUser.setUsername(updatedUser.getUsername());
        loggedUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            loggedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userService.saveUser(loggedUser);


        model.addAttribute("user", loggedUser);
        model.addAttribute("success", "Profil mis à jour !");
        return "editprofile";
    }
}
