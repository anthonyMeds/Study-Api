package com.example.demo.domain.courseRating;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseRatingDto
        (
                @Schema(description = "Course id", example = "1", required = true)
                @NotNull(message = "CourseId is requested")
                Long courseId,

                @Schema(description = "User id", example = "1", required = true)
                @NotNull(message = "User id is requested")
                Long userId,

                @Schema(description = "Course rate", example = "4", required = true)
                @Min(value = 0, message = "Course rating must be at least 0")
                @Max(value = 10, message = "Course rating must be at most 10")
                int rating,

                @Schema(description = "Feedback", example = "Great spring boot course", required = true)
                @NotBlank(message = "Feedback is requested")
                @Size(max = 255, message = "Feedback should have 255 caracters")
                String feedback
        ) {
}
