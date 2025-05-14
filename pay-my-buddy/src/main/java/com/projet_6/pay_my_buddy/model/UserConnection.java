package com.projet_6.pay_my_buddy.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users_connections")
public class UserConnection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private User friend;
}