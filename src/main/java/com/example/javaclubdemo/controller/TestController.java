package com.example.javaclubdemo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class TestController {

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('user')")
    public ResponseEntity<String> testUser() {
        return ResponseEntity.ok("hello User");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> testAdmin() {
        return ResponseEntity.ok("hello Admin");
    }

    @GetMapping
    public ResponseEntity<String> testNoPermissions() {
        return ResponseEntity.ok("no permissions needed");
    }
}
