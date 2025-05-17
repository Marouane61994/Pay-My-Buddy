package com.projet_6.pay_my_buddy.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    private Double balance;

    @Transient
    public String getFormattedBalance() {
        return String.format("%.2f", this.balance);
    }

}




