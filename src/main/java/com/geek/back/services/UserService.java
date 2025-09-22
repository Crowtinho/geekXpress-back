package com.geek.back.services;

import com.geek.back.dtos.LoginRequestDTO;
import com.geek.back.dtos.UserDTO;
import com.geek.back.dtos.UserRequestDTO;

import java.util.Optional;

public interface UserService extends CrudService<UserDTO>{

    UserDTO register(UserRequestDTO request);
    UserDTO login(LoginRequestDTO request);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findByEmail(String email);
    UserDTO update(Long id, UserRequestDTO userRequestDTO);

}
