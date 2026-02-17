package com.vdx.BTR.service;

import com.vdx.BTR.model.Role;
import com.vdx.BTR.model.User;
import com.vdx.BTR.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(User user) {
        // domyślna rola: STANDARD
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(new HashSet<>());
            user.getRoles().add(Role.STANDARD);
        }

        // haszowanie hasła
        String encoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(encoded);

        return userRepository.save(user);
    }
}
