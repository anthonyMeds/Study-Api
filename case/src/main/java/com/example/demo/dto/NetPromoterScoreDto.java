package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record NetPromoterScoreDto
        (
                @Schema(description = "Course id", example = "1", required = true)
                @NotNull(message = "CourseId is requested")
                Long courseId
        ) {
}
