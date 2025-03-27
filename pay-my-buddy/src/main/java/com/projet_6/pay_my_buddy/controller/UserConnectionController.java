package com.projet_6.pay_my_buddy.controller;



import com.projet_6.pay_my_buddy.service.UserConnectionService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @GetMapping("/add")
    public String addConnection() {
        return "add_connection";
    }

    @PostMapping("/add")
    public String saveConnection(@RequestParam String email) {
        // Logic to save connection
        return "redirect:/connections/add";
    }

}
