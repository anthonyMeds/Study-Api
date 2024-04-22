package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ResponseCourseRatingDto
        (
                @Schema(description = "Rating id", example = "1", required = true)
                Long id,
                @Schema(description = "Course id", example = "1", required = true)
                Long courseId,

                @Schema(description = "User id", example = "1", required = true)
                Long userId,

                @Schema(description = "Course rate", example = "4", required = true)
                int rating,

                @Schema(description = "Feedback", example = "Great spring boot course", required = true)
                String feedback,

                @Schema(description = "Rating Date", type = "string", example = "2024-04-20T22:05:01")
                LocalDateTime ratingDate
        ) {


}
