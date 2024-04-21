package com.example.demo.service;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.domain.users.User;
import com.example.demo.domain.users.UserRole;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.course.ResponseCourseDto;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public Page<ResponseCourseDto> getCourseByStatus(CourseStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Course> coursePage =  courseRepository.findByStatus(status, pageable);

        return coursePage.map(this::convertToDTO);
    }

    private ResponseCourseDto convertToDTO(Course course) {
        ResponseCourseDto dto = new ResponseCourseDto
                (
                        course.getName(), course.getCode(), course.getInstructor(),
                        course.getDescription(), course.getStatus(), course.getCreationDate(),
                        course.getInactivationDate()
                );

        return dto;
    }

    public ResponseCourseDto updateCourseStatus(String courseCode, CourseStatus courseStatus) throws Exception {

        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new Exception("Course not found with code: " + courseCode));

        course.setStatus(courseStatus);

        if (courseStatus.equals(CourseStatus.INACTIVE) && course.getInactivationDate() != null) {
            course.setInactivationDate(LocalDateTime.now());
        }

        courseRepository.save(course);

        return convertToDTO(course);

    }
}
