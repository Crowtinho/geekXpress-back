package com.geek.back.controllers;

import com.geek.back.dtos.LoginRequestDTO;
import com.geek.back.dtos.UserDTO;
import com.geek.back.dtos.UserRequestDTO;
import com.geek.back.services.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO request) {
        UserDTO updated = userService.update(id, request);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRequestDTO request) {
        UserDTO user = userService.register(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        UserDTO user = userService.login(request);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/resource")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("Este es un recurso protegido!");
    }

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok( "Panel de administración");
    }

}
