package com.example.javaclubdemo.controller;

import com.example.javaclubdemo.dto.UserRequestDto;
import com.example.javaclubdemo.model.entity.User;
import com.example.javaclubdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<Void> createUser(@RequestBody UserRequestDto userRequestDto,
                                           @AuthenticationPrincipal User authenticatedUser) {
       userService.createUser(userRequestDto);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteUser(@RequestParam String email,
                                           @AuthenticationPrincipal User authenticatedUser) {
        this.userService.deleteUser(email);
        return ResponseEntity.ok(null);
    }

    @PutMapping("")
    public ResponseEntity<Void> editUser(@RequestBody UserRequestDto userRequestDto) {
        this.userService.editUser(userRequestDto);
        return ResponseEntity.ok(null);
    }
}