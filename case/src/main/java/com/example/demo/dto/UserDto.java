package com.example.demo.dto;

import com.example.demo.domain.users.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record UserDto
        (
                @Schema(description = "Name of the user", example = "John Doe", required = true)
                @NotBlank(message = "Name is required")
                String name,

                @Schema(description = "Username of the user", example = "johndoe", required = true, maxLength = 20)
                @Pattern(regexp = "^[a-z]+$", message = "Username must contain only lowercase letters")
                @NotBlank(message = "Username is required")
                @Size(max = 20, message = "Username must be at most 20 characters")
                String username,

                @Schema(description = "Email of the user", example = "john@example.com", required = true, format = "email")
                @NotBlank(message = "Email is required")
                @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email format")
                String email,

                @Schema(description = "Password of the user", example = "password123", required = true, minLength = 6)
                @NotBlank(message = "Password is required")
                @Size(min = 6, message = "Password must be at least 6 characters")
                String password,

                @Schema(description = "Role of the user", example = "STUDENT", required = true)
                @NotNull(message = "Role is required")
                UserRole role,

                @Schema(description = "Creation date of the user", example = "2024-04-20T10:30:00", required = true)
                LocalDateTime creationDate
        ) {
}
