package com.cogniwide.TaskManager.admin;

import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.enums.Roles;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AdminInitialization implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "ADMIN";
        if(userRepository.findByUsername(adminUsername).isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("Krishna@2003"));
            admin.setRole(Roles.ROLE_ADMIN);  // assign admin role
            userRepository.save(admin);
            System.out.println("Admin user created!");
        }
    }
}
