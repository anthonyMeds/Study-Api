package com.example.demo.repository;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);

    Page<Course> findByStatus(CourseStatus status, Pageable pageable);
}
