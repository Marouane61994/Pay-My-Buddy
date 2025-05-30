package com.projet_6.pay_my_buddy.controller;

import com.projet_6.pay_my_buddy.model.Transaction;
import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.service.TransactionService;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
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

    @Autowired
    private UserConnectionService userConnectionService;

    @GetMapping("/send")
    public String showTransactions(Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:users/login";
        }

        List<Transaction> transactions = transactionService.getUserTransactions(loggedUser);

        List<User> relations = userConnectionService.getFriendsForUser(loggedUser);
        model.addAttribute("user", loggedUser);
        model.addAttribute("transactions", transactions);
        model.addAttribute("relations", relations);

        return "transfer";
    }

    @PostMapping("/send")
    public String sendMoney(@RequestParam String receiverEmail,
                            @RequestParam double amount,
                            @RequestParam String description,
                            Model model) {
        User loggedUser = userService.getCurrentUser();
        if (loggedUser == null) {
            return "redirect:/user/login";
        }

        boolean success = transactionService.sendMoney(loggedUser.getEmail(), receiverEmail, amount, description);
        if (!success) {
            model.addAttribute("error", "Transaction échouée. Vérifiez les informations.");
            return showTransactions(model);
        }

        return "redirect:/transactions/send";
    }

}

