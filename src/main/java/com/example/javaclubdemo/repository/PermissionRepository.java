package com.example.javaclubdemo.repository;


import com.example.javaclubdemo.model.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
