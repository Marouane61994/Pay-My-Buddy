package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/connections")
public class UserConnectionController {

    @Autowired
    private UserService userService;

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
    public String addRelation(@RequestParam String friendEmail, HttpSession session, Model model) {
        User loggedUser = userService.getCurrentUser();

        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        boolean added = userService.getAllUsers().add(loggedUser);

        model.addAttribute("success", "Ami ajouté avec succès !");

        model.addAttribute("friends", loggedUser.getEmail());
        return "addrelation"; // recharge la page des amis
    }
}
