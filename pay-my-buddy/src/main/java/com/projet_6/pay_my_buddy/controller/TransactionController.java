package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.TransactionService;
import com.projet_6.pay_my_buddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/send")
    public String showTransactions(Model model, HttpSession session) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "transfer";
        }

        List<Transaction> transactions = transactionService.getUserTransactions(loggedUser);
        // Récupère la liste des amis pour l'affichage du formulaire
       // List<User> friends = userService.getAllUsers();

        model.addAttribute("transactions", transactions);
        model.addAttribute("relations",loggedUser);
        return "transfer";
    }

    @PostMapping("/send")
    public String sendMoney(@RequestParam String receiverEmail,
                            @RequestParam double amount,
                            @RequestParam String description,
                            HttpSession session,
                            Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:/user/login";
        }

        boolean success = transactionService.sendMoney(loggedUser.getEmail(), receiverEmail, amount, description);
        if (!success) {
            model.addAttribute("error", "Transaction échouée. Vérifiez les informations.");
            return showTransactions(model, session); // Recharge la page avec un message d'erreur
        }

        return "redirect:/transactions/send";
    }




}

