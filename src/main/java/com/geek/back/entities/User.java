package com.geek.back.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id",nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ShoppingCart shoppingCart;




}
