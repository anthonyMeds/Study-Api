package com.example.demo.domain.enrollment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ResponseEnrollmentDto
        (
                @Schema(description = "Enrollment Id", example = "1")
                Long id,

                @Schema(description = "User Id", example = "1")
                Long userId,

                @Schema(description = "Course Id", example = "1")
                Long courseId,

                @Schema(description = "Enrollment Date", type = "string", example = "2024-04-20T22:05:01")
                LocalDateTime enrollment_date
        ) {
}
