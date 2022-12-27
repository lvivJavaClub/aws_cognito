package com.example.javaclubdemo.service;


import com.example.javaclubdemo.dto.UserRequestDto;
import com.example.javaclubdemo.model.entity.Role;
import com.example.javaclubdemo.model.entity.User;
import com.example.javaclubdemo.repository.RoleRepository;
import com.example.javaclubdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CognitoService cognitoService;

    public void createUser(UserRequestDto userRequestDto) {
        cognitoService.createUser(userRequestDto);
        saveUserInDatabase(userRequestDto);
    }

    private void saveUserInDatabase(UserRequestDto userRequestDto) {
        Optional<Role> roleOptional = roleRepository.findById(userRequestDto.getRoleId());
        User newUser = User.builder()
                .email(userRequestDto.getEmail())
                .role(roleOptional.get())
                .confirmed(false)
                .build();
        userRepository.save(newUser);
    }


    public void editUser(UserRequestDto userRequestDto) {
        Optional<User> userOptional = userRepository.findByEmail(userRequestDto.getEmail());
        Optional<Role> roleOptional = roleRepository.findById(userRequestDto.getRoleId());
        if (userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            user.setRole(roleOptional.get());
            userRepository.save(user);
        }
    }

    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User :" + email));

        cognitoService.deleteUser(user.getEmail());
        userRepository.delete(user);
    }

}
