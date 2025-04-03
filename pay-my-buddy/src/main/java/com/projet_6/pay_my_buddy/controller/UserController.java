package com.projet_6.pay_my_buddy.controller;



import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

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
            return "register"; // Retourne le formulaire avec erreurs
        }

        // Vérifie si l'email existe déjà
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Cet email est déjà utilisé !");
            return "register";
        }

        userService.saveUser(user);
        return "redirect:/user/login";
    }

    // Affiche le formulaire de connexion
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Gère la connexion utilisateur (temporairement sans authentification)
    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return "redirect:/home"; // Rediriger vers la page d'accueil après connexion réussie
            } else {
                model.addAttribute("error", "Mot de passe incorrect");
            }
        } else {
            model.addAttribute("error", "Utilisateur non trouvé");
        }

        return "login"; // Retour à la page de connexion en cas d'échec
    }

    // Affiche la page de profil de l'utilisateur connecté
    @GetMapping("/profile")
    public String showProfile(Model model, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("user", loggedUser);
        return "profile";
    }

    // Déconnexion
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }
}


