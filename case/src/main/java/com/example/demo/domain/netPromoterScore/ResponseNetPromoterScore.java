package com.example.demo.domain.netPromoterScore;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseNetPromoterScore
        (
                @Schema(description = "Net Promoter Score (NPS) value", example = "75.0")
                float npsScore,

                @Schema(description = "NPS classification", example = "Excellent")
                String classification,
                @Schema(description = "Course id", example = "1")
                Long courseId,

                @Schema(description = "Name of the course", example = "Spring Boot Advanced")
                String name
        ) {
}
