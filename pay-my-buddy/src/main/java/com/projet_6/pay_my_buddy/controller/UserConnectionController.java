package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.User;

import com.projet_6.pay_my_buddy.service.UserConnectionService;
import com.projet_6.pay_my_buddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/connections")
public class UserConnectionController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserConnectionService userConnectionService;

    // Affiche la page de connexion d'amis
    @GetMapping
    public String showConnections(HttpSession session, Model model) {
        User loggedUser = userService.getCurrentUser();

        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("friends", loggedUser.getEmail());
        return "addrelation";
    }

    // Ajouter une relation (ami)
    @PostMapping("/add-relation")
    public String addRelation(@RequestParam String friendEmail, Model model) {
        User loggedUser = userService.getCurrentUser();

        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        Optional<User> friendOptional = userService.findByEmail(friendEmail);

        if (friendOptional.isEmpty()) {
            model.addAttribute("error", "Aucun utilisateur avec cet email.");
            return "addrelation";
        }

        User friend = friendOptional.get();

        boolean alreadyConnected = userConnectionService.existsConnection(loggedUser, friend);

        if (alreadyConnected) {
            model.addAttribute("info", "Vous êtes déjà connecté à cet utilisateur.");
            return "addrelation";
        }

        userConnectionService.addConnection(loggedUser, friend);
        model.addAttribute("success", "Ami ajouté avec succès !");
        return "addrelation";
    }


}
