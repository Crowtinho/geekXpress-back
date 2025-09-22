package com.geek.back.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Long id;
    private String userName;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private String password;
    private String token;
}
