package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
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

    @Autowired
    private UserConnectionService userConnectionService;

    @GetMapping
    public String showConnections(Model model) {
        User loggedUser = userService.getCurrentUser();

        if (loggedUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("friends", loggedUser.getEmail());
        return "addrelation";
    }

    @PostMapping("/add-relation")
    public String addRelation(@RequestParam String friendEmail, Model model) {
        User loggedUser = userService.getCurrentUser();

        if (loggedUser == null) {
            return "redirect:/users/login";
        }
        try {
            userConnectionService.addRelation(loggedUser, friendEmail);
            return "redirect:/transactions/send";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "addrelation";
        }

    }
}
