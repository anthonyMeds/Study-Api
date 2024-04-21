package com.example.demo.dto.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record EnrollmentDto
        (
                @Schema(description = "User ID", example = "1", required = true)
                @NotNull(message = "User ID is required")
                Long userId,

                @Schema(description = "Course ID", example = "1", required = true)
                @NotNull(message = "Course ID is required")
                Long courseId
        ) {
}
