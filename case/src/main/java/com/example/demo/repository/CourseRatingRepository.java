package com.example.demo.repository;

import com.example.demo.domain.courseRating.CourseRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {
}
