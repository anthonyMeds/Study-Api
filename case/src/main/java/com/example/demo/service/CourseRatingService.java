package com.example.demo.service;

import com.example.demo.domain.courseRating.CourseRating;
import com.example.demo.dto.CourseRatingDto;
import com.example.demo.dto.ResponseCourseRatingDto;
import com.example.demo.domain.courses.Course;
import com.example.demo.domain.users.User;
import com.example.demo.repository.CourseRatingRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseRatingService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    public ResponseCourseRatingDto createCourseRating(CourseRatingDto courseRatingDto) throws Exception {

        User user = userService.getUserById(courseRatingDto.userId());
        Course course = courseService.getActiveCourseById(courseRatingDto.courseId());

        enrollmentService.ensureUserIsEnrolled(user, course);

        ensureNoPreviousRatingForUser(user, course);

        CourseRating courseRating = saveCourseRating(courseRatingDto, course, user);

        sendNotificationIfNeeded(course, courseRating);

        return convertToDto(courseRating);
    }

    public void ensureNoPreviousRatingForUser(User user, Course course) throws Exception {
        if (courseRatingRepository.existsByUserAndCourse(user, course)) {
            throw new Exception("User has already rated this course.");
        }
    }

    public CourseRating saveCourseRating(CourseRatingDto courseRatingDto, Course course, User user) {
        CourseRating courseRating = new CourseRating(courseRatingDto, course, user);
        return courseRatingRepository.save(courseRating);
    }


    public void sendNotificationIfNeeded(Course course, CourseRating courseRating) throws Exception {
        if (courseRating.getRating() < 6) {
            sendMail(course, courseRating);
        }
    }


    public void sendMail(Course course, CourseRating courseRating) throws Exception {

        Optional<User> instructorData = userRepository.findUserByUsername(course.getInstructor());

        String instructorEmail = instructorData.map(User::getEmail)
                .orElseThrow(() -> new Exception("Email not found for instructor"));

        String subject = "Course feedback";

        EmailSenderService.send(instructorEmail, subject, courseRating.getFeedback());

    }

    private ResponseCourseRatingDto convertToDto(CourseRating courseRating) {

        return new ResponseCourseRatingDto
                (
                        courseRating.getId(), courseRating.getCourse().getId(), courseRating.getUser().getId(),
                        courseRating.getRating(), courseRating.getFeedback(), courseRating.getRatingDate()
                );

    }

    public List<CourseRating> getAllCoursesRates() throws Exception {

        List<CourseRating> courseRatingList = courseRatingRepository.findAll();

        if (courseRatingList.isEmpty()) {
            throw new Exception( "Course rates not found");
        }

        return courseRatingList;

    }
}
