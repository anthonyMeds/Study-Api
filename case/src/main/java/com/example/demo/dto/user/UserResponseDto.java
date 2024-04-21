package com.example.demo.dto.user;

import com.example.demo.domain.users.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserResponseDto        (
        @Schema(description = "Name of the user", example = "John Doe", required = true)
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Email of the user", example = "john@example.com", required = true, format = "email")
        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^(.+)@(.+)$", message = "Invalid email format")
        String email,

        @Schema(description = "Role of the user", example = "STUDENT", required = true)
        @NotNull(message = "Role is required")
        UserRole role
) {
}
