package com.projet_6.pay_my_buddy.controller;


import com.projet_6.pay_my_buddy.model.User;
import com.projet_6.pay_my_buddy.model.UserConnection;
import com.projet_6.pay_my_buddy.service.UserConnectionService;
import com.projet_6.pay_my_buddy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/connections")
public class UserConnectionController {

    @Autowired
    private UserConnectionService userConnectionService;

    @Autowired
    private UserService userService;

    @GetMapping("/add")
    public String showAddConnectionForm(Model model) {
        model.addAttribute("UserConnection", new UserConnection());
        return "add-connection";
    }

    @PostMapping("/add")
    public String addConnection(@ModelAttribute UserConnection userConnection,

                                Model model) {

        String email = userConnection.getUser().getUsername();
        User user = userService.getUserByEmail(email);

        //User friend = userService.getUserById(;

       // userConnectionService.addConnection(user, friend);

        model.addAttribute("success", "Relation ajoutée !");
        return "add-connection";
    }

}
