package com.example.demo.service;

import com.example.demo.domain.courseRating.CourseRating;
import com.example.demo.domain.courseRating.CourseRatingDto;
import com.example.demo.domain.courseRating.ResponseCourseRatingDto;
import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.domain.users.User;
import com.example.demo.repository.CourseRatingRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    public ResponseCourseRatingDto createCourseRating(CourseRatingDto courseRatingDto) throws Exception {

        User user = userRepository.findById(courseRatingDto.userId())
                .orElseThrow(() -> new Exception("User not found with ID: " + courseRatingDto.userId()));


        Course course = courseRepository.findByIdAndStatus(courseRatingDto.courseId(), CourseStatus.ACTIVE)
                .orElseThrow(() -> new Exception("Active course not found with ID: " + courseRatingDto.courseId()));

        boolean isEnrolled = enrollmentRepository.existsByUserAndCourse(user, course);
        if (!isEnrolled) {
            throw new Exception("User is not enrolled in this course.");
        }

        CourseRating courseRating = new CourseRating(courseRatingDto, course, user);
        courseRatingRepository.save(courseRating);

        return convertToDto(courseRating);

    }

    private ResponseCourseRatingDto convertToDto(CourseRating courseRating) {

        return new ResponseCourseRatingDto
                (
                        courseRating.getId(), courseRating.getCourse().getId(), courseRating.getUser().getId(),
                        courseRating.getRating(), courseRating.getFeedback(), courseRating.getRatingDate()
                );

    }
}
