package com.example.demo.service;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.domain.users.User;
import com.example.demo.domain.users.UserRole;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public Course createCourse(CourseDto courseDto) throws Exception {

        Course course = new Course(courseDto);

        validateCourseDoesNotExist(course.getCode());

        validateInstructor(course.getInstructor());

        courseRepository.save(course);

        return course;
    }

    public void validateCourseDoesNotExist(String code) throws Exception {
        if (courseRepository.existsByCode(code)) {
            throw new Exception("Course already exists.");
        }
    }

    public void validateInstructor(String instructorUsername) throws Exception {
        Optional<User> userOptional = userRepository.findUserByUsername(instructorUsername);
        User instructor = userOptional.orElseThrow(() -> new Exception("Instructor not found."));

        if (!instructor.getRole().equals(UserRole.INSTRUCTOR)) {
            throw new Exception("Only instructors can create courses.");
        }

    }

    public Page<Course> getCourseByStatus(CourseStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return courseRepository.findByStatus(status, pageable);
    }
}
