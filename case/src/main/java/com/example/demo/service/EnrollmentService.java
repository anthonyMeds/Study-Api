package com.example.demo.service;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.domain.enrollment.Enrollment;
import com.example.demo.domain.enrollment.ResponseEnrollmentDto;
import com.example.demo.domain.users.User;
import com.example.demo.dto.enrollment.EnrollmentDto;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {


    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;


    public ResponseEnrollmentDto createRegistration(EnrollmentDto enrollmentDto) throws Exception {

        User user = userRepository.findById(enrollmentDto.userId())
                .orElseThrow(() -> new Exception("User not found with ID: " + enrollmentDto.userId()));

        Course course = courseRepository.findByIdAndStatus(enrollmentDto.courseId(), CourseStatus.ACTIVE)
                .orElseThrow(() -> new Exception("Active course not found with ID: " + enrollmentDto.courseId()));

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUserAndCourse(user, course);
        if (existingEnrollment.isPresent()) {
            throw new Exception("User is already enrolled in this course.");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        enrollmentRepository.save(enrollment);

        return convertToDTO(enrollment);
    }


    private ResponseEnrollmentDto convertToDTO(Enrollment enrollment) {
        ResponseEnrollmentDto dto = new ResponseEnrollmentDto
                (
                        enrollment.getId(), enrollment.getUser().getId(), enrollment.getCourse().getId(),
                        enrollment.getEnrollmentDate()
                );

        return dto;
    }

    public List<Enrollment> getAllEnrollment() throws Exception {

        List<Enrollment> enrollmentList = enrollmentRepository.findAll();


        if (enrollmentList.isEmpty()) {
            throw new Exception("Enrollment not found");
        }

        return enrollmentList;
    }
}
