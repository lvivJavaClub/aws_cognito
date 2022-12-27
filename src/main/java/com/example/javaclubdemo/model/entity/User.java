package com.example.javaclubdemo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user", schema = "public", catalog = "playground")
public class User extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    @JsonIgnore
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String userName;

    @ManyToOne
    private Role role;

    @Column(name = "is_confirmed")
    private Boolean confirmed;

}
