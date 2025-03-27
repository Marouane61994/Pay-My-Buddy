package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping("/send")
    public ResponseEntity<String> transferMoney(@RequestParam int senderId, @RequestParam int receiverId, @RequestParam Double amount) {
        String result = transactionService.sendMoney(senderId, receiverId, amount);
        if (result.equals("Transaction réussie.")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
}

