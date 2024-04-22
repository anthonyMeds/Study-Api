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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;


    @Test
    @DisplayName("Test createRegistration method - User not found")
    void testCreateRegistration_UserNotFound() {
        EnrollmentDto enrollmentDto = new EnrollmentDto(1L, 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> enrollmentService.createRegistration(enrollmentDto));
        assertEquals("User not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, never()).findByIdAndStatus(anyLong(), any(CourseStatus.class));
        verify(enrollmentRepository, never()).findByUserAndCourse(any(User.class), any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Test createRegistration method - Course not found")
    void testCreateRegistration_CourseNotFound() {
        EnrollmentDto enrollmentDto = new EnrollmentDto(1L, 1L);
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findByIdAndStatus(1L, CourseStatus.ACTIVE)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> enrollmentService.createRegistration(enrollmentDto));
        assertEquals("Active course not found with ID: 1", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findByIdAndStatus(1L, CourseStatus.ACTIVE);
        verify(enrollmentRepository, never()).findByUserAndCourse(any(User.class), any(Course.class));
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Test createRegistration method - User already enrolled")
    void testCreateRegistration_UserAlreadyEnrolled() {
        EnrollmentDto enrollmentDto = new EnrollmentDto(1L, 1L);
        User user = new User();
        Course course = new Course();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findByIdAndStatus(1L, CourseStatus.ACTIVE)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByUserAndCourse(user, course)).thenReturn(Optional.of(new Enrollment()));

        Exception exception = assertThrows(Exception.class, () -> enrollmentService.createRegistration(enrollmentDto));
        assertEquals("User is already enrolled in this course.", exception.getMessage());

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findByIdAndStatus(1L, CourseStatus.ACTIVE);
        verify(enrollmentRepository, times(1)).findByUserAndCourse(user, course);
        verify(enrollmentRepository, never()).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Test createRegistration method - Success")
    void testCreateRegistration_Success() throws Exception {
        EnrollmentDto enrollmentDto = new EnrollmentDto(1L, 1L);
        User user = new User();
        Course course = new Course();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findByIdAndStatus(1L, CourseStatus.ACTIVE)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByUserAndCourse(user, course)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(new Enrollment());

        ResponseEnrollmentDto responseEnrollmentDto = enrollmentService.createRegistration(enrollmentDto);

        assertNotNull(responseEnrollmentDto);

        verify(userRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).findByIdAndStatus(1L, CourseStatus.ACTIVE);
        verify(enrollmentRepository, times(1)).findByUserAndCourse(user, course);
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    @DisplayName("Test ensureUserIsEnrolled method - User is enrolled")
    void testEnsureUserIsEnrolled_UserEnrolled() throws Exception {

        User user = new User();
        Course course = new Course();
        when(enrollmentRepository.existsByUserAndCourse(user, course))
                .thenReturn(true);

        enrollmentService.ensureUserIsEnrolled(user, course);
        // No exception thrown indicates success
    }

    @Test
    @DisplayName("Test ensureUserIsEnrolled method - User is not enrolled")
    void testEnsureUserIsEnrolled_UserNotEnrolled() {

        User user = new User();
        Course course = new Course();
        when(enrollmentRepository.existsByUserAndCourse(user, course))
                .thenReturn(false);

        Exception exception = assertThrows(Exception.class, () ->
                enrollmentService.ensureUserIsEnrolled(user, course)
        );
        assertEquals("User is not enrolled in this course.", exception.getMessage());
    }

}
