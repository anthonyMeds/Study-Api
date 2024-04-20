package com.example.demo.service;

import com.example.demo.domain.users.User;
import com.example.demo.domain.users.UserRole;
import com.example.demo.repository.CourseRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CourseServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    @DisplayName("Test validateCourseDoesNotExist method - Course does not exist")
    void testValidateCourseDoesNotExist_CourseDoesNotExist() throws Exception {
        String nonExistingCode = "nonExistingCode";
        when(courseRepository.existsByCode(nonExistingCode)).thenReturn(false);

        // No exception should be thrown because the course does not exist
        courseService.validateCourseDoesNotExist(nonExistingCode);
    }

    @Test
    @DisplayName("Test validateCourseDoesNotExist method - Course exists")
    void testValidateCourseDoesNotExist_CourseExists() {
        String existingCode = "existingCode";
        when(courseRepository.existsByCode(existingCode)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () ->
                courseService.validateCourseDoesNotExist(existingCode));

        String expectedMessage = "Course already exists.";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Test validateInstructor method - Instructor exists and is an instructor")
    void testValidateInstructor_InstructorExistsAndIsInstructor() throws Exception {
        String instructorUsername = "instructorUsername";
        User instructor = new User();
        instructor.setUsername(instructorUsername);
        instructor.setRole(UserRole.INSTRUCTOR);
        when(userRepository.findUserByUsername(instructorUsername)).thenReturn(Optional.of(instructor));

        // No exception should be thrown because the instructor exists and is an instructor - role
        courseService.validateInstructor(instructorUsername);
    }

    @Test
    @DisplayName("Test validateInstructor method - Instructor exists but is not an instructor")
    void testValidateInstructor_InstructorExistsButIsNotInstructor() {
        String instructorUsername = "instructorUsername";
        User instructor = new User();
        instructor.setUsername(instructorUsername);
        instructor.setRole(UserRole.STUDENT);
        when(userRepository.findUserByUsername(instructorUsername)).thenReturn(Optional.of(instructor));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> courseService.validateInstructor(instructorUsername));

        String expectedMessage = "Only instructors can create courses.";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Test validateInstructor method - Instructor does not exist")
    void testValidateInstructor_InstructorDoesNotExist() {
        String instructorUsername = "nonExistingInstructor";
        when(userRepository.findUserByUsername(instructorUsername)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> courseService.validateInstructor(instructorUsername));

        String expectedMessage = "Instructor not found.";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
