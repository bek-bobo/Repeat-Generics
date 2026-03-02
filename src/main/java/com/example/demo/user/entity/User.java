package com.example.demo.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "'user'")
public class User {

    @Id
    private UUID id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
}
