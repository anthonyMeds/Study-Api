package com.example.demo.dto.course;

import com.example.demo.domain.courses.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ResponseCourseDto
        (
                @Schema(description = "Name of the course", example = "Spring Boot Advanced", required = true)
                @NotBlank(message = "Name is required")
                String name,

                @Schema(description = "Code of the course", example = "java-boot", required = true, maxLength = 10)
                @Pattern(regexp = "^[a-zA-Z-]+$", message = "Code must contain only alphabetic characters and hyphens")
                @NotBlank(message = "Code is required")
                @Size(max = 10, message = "Code must be at most 10 characters")
                String code,

                @Schema(description = "Name of the instructor", example = "Joao", required = true)
                @NotBlank(message = "Instructor name is required")
                String instructor,

                @Schema(description = "Description of the course", example = "Advanced concepts in Spring Boot", required = true)
                @NotBlank(message = "Description is required")
                String description,

                @Schema(description = "Status of the course", example = "ACTIVE", required = true)
                @NotNull(message = "Status is required")
                CourseStatus status,

                @Schema(description = "Date of course creation", required = true)
                @NotNull(message = "Creation date is required")
                LocalDateTime creationDate,

                @Schema(description = "Date of course inactivation")
                LocalDateTime inactivationDate

        ) {
}
