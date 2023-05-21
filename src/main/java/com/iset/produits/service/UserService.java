package com.iset.produits.service;

import antlr.BaseAST;
import com.iset.produits.dao.RoleRepository;
import com.iset.produits.dao.UserRepository;
import com.iset.produits.entities.Role;
import com.iset.produits.entities.User;
import com.iset.produits.security.Config;
import com.iset.produits.security.SecurityConfig;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private Config config;
    @Autowired
    RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
            Objects.requireNonNull(username);
        return userRepository.findUserWithName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public User saveUser(String username, String password, String confirmedPassword) {
        User appUser = new User();
        if (userRepository.findUserWithName(username).isPresent())
            throw new RuntimeException("User already exists");
        if (!password.equals(confirmedPassword))
            throw new RuntimeException("Please confirm your password");
        appUser.setUsername(username);
        Set<Role> roles = new HashSet<Role>();
        Role r = new Role("ROLE_USER");
        roleRepository.save(r);
        roles.add(r);
        appUser.setRoles(roles);
        appUser.setPassword(config.bCryptPasswordEncoder().encode(password));
        userRepository.save(appUser);
        return appUser;
    }
}

