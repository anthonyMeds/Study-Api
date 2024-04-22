package com.example.demo.service;

import com.example.demo.domain.courseRating.CourseRating;
import com.example.demo.domain.courses.Course;
import com.example.demo.domain.users.User;
import com.example.demo.repository.CourseRatingRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class CourseRatingServiceTest {



    @Mock
    private UserRepository userRepository;


    @Mock
    private CourseRatingRepository courseRatingRepository;

    @InjectMocks
    private CourseRatingService courseRatingService;


    @Test
    @DisplayName("Test ensureNoPreviousRatingForUser method - User has already rated the course")
    void testEnsureNoPreviousRatingForUser_UserHasRatedCourse() {

        User user = new User();
        Course course = new Course();
        when(courseRatingRepository.existsByUserAndCourse(user, course)).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () ->
                courseRatingService.ensureNoPreviousRatingForUser(user, course)
        );

        String expectedMessage = "User has already rated this course.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Test ensureNoPreviousRatingForUser method - User has not rated the course")
    void testEnsureNoPreviousRatingForUser_UserHasNotRatedCourse() {

        User user = new User();
        Course course = new Course();
        when(courseRatingRepository.existsByUserAndCourse(user, course)).thenReturn(false);

        assertDoesNotThrow(() ->
                courseRatingService.ensureNoPreviousRatingForUser(user, course)
        );
    }


    @Test
    @DisplayName("Test sendMail method - Email sent successfully")
    void testSendMail_EmailSentSuccessfully() throws Exception {

        Course course = new Course();
        course.setInstructor("instructorUsername");

        User instructor = new User();
        instructor.setEmail("instructor@example.com");

        CourseRating courseRating = new CourseRating();
        courseRating.setFeedback("Feedback text");

        when(userRepository.findUserByUsername(course.getInstructor())).thenReturn(Optional.of(instructor));

        courseRatingService.sendMail(course, courseRating);

        EmailSenderService.send("instructor@example.com", "Course feedback", "Feedback text");
    }

    @Test
    @DisplayName("Test sendMail method - Instructor email not found")
    void testSendMail_InstructorEmailNotFound() {

        Course course = new Course();
        course.setInstructor("instructorUsername");

        when(userRepository.findUserByUsername(course.getInstructor())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> courseRatingService.sendMail(course, new CourseRating()));

        String expectedMessage = "Email not found for instructor";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


}
