package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
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
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new User());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute User loginRequest, Model model) {
        // Exemple : vérification simple, à remplacer par votre service d'authentification
        if ("user@example.com".equals(loginRequest.getEmail()) && "password".equals(loginRequest.getPassword())) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect.");
            return "redirect:/profile";
        }

    }

}

