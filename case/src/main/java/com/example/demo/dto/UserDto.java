package com.example.demo.dto;

import com.example.demo.domain.users.UserRole;

import java.time.LocalDateTime;

public record UserDto
        (
                String name,
                String username,
                String email,
                String password,
                UserRole role,
                LocalDateTime creationDate
        ) {
}
