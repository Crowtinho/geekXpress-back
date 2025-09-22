package com.geek.back.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String name;

//    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
//    private Set<User> users = new HashSet<>();


}
