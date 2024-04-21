package com.example.demo.repository;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.enrollment.Enrollment;
import com.example.demo.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {


    Optional<Enrollment> findByUserAndCourse(User user, Course course);
}
