package com.example.javaclubdemo.model.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

import static com.example.javaclubdemo.util.AuditionUtil.getAuditor;


@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = getAuditor();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.updatedBy = getAuditor();
    }
}