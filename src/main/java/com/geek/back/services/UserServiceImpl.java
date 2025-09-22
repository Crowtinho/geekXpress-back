package com.geek.back.services;

import com.geek.back.repositories.UserRepository;
import com.geek.back.dtos.LoginRequestDTO;
import com.geek.back.dtos.UserDTO;
import com.geek.back.dtos.UserRequestDTO;
import com.geek.back.entities.Role;
import com.geek.back.entities.ShoppingCart;
import com.geek.back.entities.User;
import com.geek.back.jwt.JwtUtil;
import com.geek.back.repositories.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private RoleRepository roleRepository;
    final private PasswordEncoder passwordEncoder;
    final private JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserRequestDTO userDTO) {
        return userRepository.findById(id)
                .map(e ->{
                    e.setUserName(userDTO.getUserName());
                    e.setName(userDTO.getName());
                    e.setLastName(userDTO.getLastName());
                    e.setEmail(userDTO.getEmail());

                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        e.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                    }

                    return toDTO(userRepository.save(e));

                }).orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.findById(id).map(u -> {
            userRepository.deleteById(id);
            return u;
        }).orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Override
    public UserDTO register(UserRequestDTO request) {
        // Validar username y email
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Crear entidad User
        User user = new User();
        user.setUserName(request.getUserName());
        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Encriptar password antes de guardar
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Asignar rol por defecto ROLE_USER
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);

        // Crear carrito asociado al usuario
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);          // Vincular carrito al usuario
        user.setShoppingCart(cart);  // Vincular usuario al carrito

        // Guardar y devolver DTO
        return toDTOWithRole(userRepository.save(user));
    }


    @Override
    public UserDTO login(LoginRequestDTO request) {
        return userRepository.findByUserName(request.getUserName())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(u -> {
                    // mapea datos del usuario
                    UserDTO dto = toDTO(u);
                    // generar token y agregarlo al DTO
                    String token = jwtUtil.generateToken(u.getUserName(), u.getRole().getName());
                    dto.setToken(token); // asegúrate de tener un campo `token` en UserDTO
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByUsername(String username) {
        return userRepository.findByUserName(username).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDTO);
    }


    // carga de usuario implementado desde UserDetailsService
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado"));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().getName())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }

    private UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
//                .role(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    private UserDTO toDTOWithRole(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole() != null ? user.getRole().getName() : null)
                .build();
    }

    private User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUserName(dto.getUserName());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        // el password normalmente no debería mapearse aquí directo desde el DTO
        return user;
    }

}
