package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Affiche le formulaire d'inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Gère l'inscription d'un utilisateur
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "Cet email est déjà utilisé !");
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/users/login";
    }

    // Affiche le formulaire de connexion
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Gère la connexion utilisateur
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                session.setAttribute("loggedUser", user);
                return "redirect:/users/profile";
            } else {
                model.addAttribute("error", "Mot de passe incorrect");
            }
        } else {
            model.addAttribute("error", "Utilisateur non trouvé");
        }

        return "login";
    }

    // Affiche la page de profil
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedUser = userService.Authentification();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", loggedUser);
        return "profile";
    }

    // Formulaire d'édition de profil
    @GetMapping("/profile/edit")
    public String editProfileForm(HttpSession session, Model model) {
        User loggedUser = userService.Authentification();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("user", loggedUser);
        return "editProfile";
    }

    // Mise à jour du profil
    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User updatedUser,
                                HttpSession session,
                                Model model) {
        User loggedUser = userService.Authentification();
        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        loggedUser.setEmail(updatedUser.getEmail());
        loggedUser.setPassword(updatedUser.getPassword()); // Ajouter encodage en production
        userService.saveUser(loggedUser);
        session.setAttribute("loggedUser", loggedUser);

        model.addAttribute("user", loggedUser);
        model.addAttribute("success", "Profil mis à jour !");
        return "profile";
    }

    // Déconnexion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }


}
