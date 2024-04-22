package com.example.demo.service;

import com.example.demo.domain.courseRating.CourseRating;
import com.example.demo.domain.courses.Course;
import com.example.demo.domain.netPromoterScore.NetPromoterScore;
import com.example.demo.domain.netPromoterScore.ResponseNetPromoterScore;
import com.example.demo.domain.users.User;
import com.example.demo.dto.CourseRatingDto;
import com.example.demo.repository.CourseRatingRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.NetPromoterScoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class NetPromoterScoreServiceTest {

    @Mock
    private NetPromoterScoreRepository netPromoterScoreRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseRatingRepository courseRatingRepository;

    @InjectMocks
    private NetPromoterScoreService netPromoterScoreService;


    @Test
    @DisplayName("Test Get All Net Promoter Scores - Non-empty List")
    public void testGetAllNetPromoterScores_NonEmptyList() throws Exception {

        Course course1 = new Course();
        course1.setId(1L);
        NetPromoterScore nps1 = new NetPromoterScore(course1, 75.0f, "Excellent");
        nps1.setId(1L);
        nps1.setSurveyDate(LocalDate.now());

        Course course2 = new Course();
        course2.setId(2L);
        NetPromoterScore nps2 = new NetPromoterScore(course2, 60.0f, "Very good");
        nps2.setId(2L);
        nps2.setSurveyDate(LocalDate.now());

        List<NetPromoterScore> mockNpsList = new ArrayList<>();
        mockNpsList.add(nps1);
        mockNpsList.add(nps2);

        when(netPromoterScoreRepository.findAll()).thenReturn(mockNpsList);

        List<ResponseNetPromoterScore> responseList = netPromoterScoreService.getAllNetPromoterScores();

        assertEquals(2, responseList.size());
        assertEquals(1L, responseList.get(0).courseId());
        assertEquals("Excellent", responseList.get(0).classification());
        assertEquals(75.0f, responseList.get(0).npsScore());
        assertEquals(2L, responseList.get(1).courseId());
        assertEquals("Very good", responseList.get(1).classification());
        assertEquals(60.0f, responseList.get(1).npsScore());
    }

    @Test
    @DisplayName("Test Get All Net Promoter Scores - Empty List")
    public void testGetAllNetPromoterScores_EmptyList() {

        List<NetPromoterScore> mockEmptyNpsList = new ArrayList<>();

        when(netPromoterScoreRepository.findAll()).thenReturn(mockEmptyNpsList);

        Exception exception = assertThrows(Exception.class, () -> netPromoterScoreService
                .getAllNetPromoterScores());

        assertEquals("Net Promoter Score not found.", exception.getMessage());
    }

    @Test
    @DisplayName("Test classifyNPS - Excellent")
    public void testClassifyNPS_Excellent() {

        float nps = 80.0f;

        String classification = netPromoterScoreService.classifyNPS(nps);

        assertEquals("Excellent", classification);
    }

    @Test
    @DisplayName("Test classifyNPS - Very good")
    public void testClassifyNPS_VeryGood() {

        float nps = 60.0f;

        String classification = netPromoterScoreService.classifyNPS(nps);

        assertEquals("Very good", classification);
    }

    @Test
    @DisplayName("Test classifyNPS - Reasonable")
    public void testClassifyNPS_Reasonable() {

        float nps = 20.0f;

        String classification = netPromoterScoreService.classifyNPS(nps);

        assertEquals("Reasonable", classification);
    }

    @Test
    @DisplayName("Test classifyNPS - Bad")
    public void testClassifyNPS_Bad() {

        float nps = -10.0f;

        String classification = netPromoterScoreService.classifyNPS(nps);

        assertEquals("Bad", classification);
    }

    @Test
    @DisplayName("Test calculateNps - Non-zero respondents")
    public void testCalculateNps_NonZeroRespondents() throws Exception {

        int promoters = 30;
        int detractors = 8;
        int totalRespondents = 50;

        float nps = netPromoterScoreService.calculateNps(promoters, detractors, totalRespondents);

        assertEquals(44.0f, nps);
    }

    @Test
    @DisplayName("Test calculateNps - Zero respondents")
    public void testCalculateNps_ZeroRespondents() {

        int promoters = 0;
        int detractors = 0;
        int totalRespondents = 0;

        assertThrows(Exception.class, () ->
                        netPromoterScoreService.calculateNps(promoters, detractors, totalRespondents),
                "The course has no rating yet."
        );
    }

    @Test
    @DisplayName("Test countPromoters - Non-empty list")
    public void testCountPromoters_NonEmptyList() {

        List<CourseRating> ratings = new ArrayList<>();
        ratings.add(new CourseRating(new CourseRatingDto(1L,
                1L, 9, "Great"), new Course(), new User()));
        ratings.add(new CourseRating(new CourseRatingDto(2L,
                2L, 10, "Excellent"), new Course(), new User()));
        ratings.add(new CourseRating(new CourseRatingDto(3L,
                3L, 8, "Good"), new Course(), new User()));

        int promoters = netPromoterScoreService.countPromoters(ratings);

        assertEquals(2, promoters);
    }

    @Test
    @DisplayName("Test countPromoters - Empty list")
    public void testCountPromoters_EmptyList() {

        List<CourseRating> ratings = new ArrayList<>();

        int promoters = netPromoterScoreService.countPromoters(ratings);

        assertEquals(0, promoters);
    }

    @Test
    @DisplayName("Test countDetractors - Non-empty list")
    public void testCountDetractors_NonEmptyList() {

        List<CourseRating> ratings = new ArrayList<>();
        ratings.add(new CourseRating(new CourseRatingDto(1L,
                1L, 3, "Poor"), new Course(), new User()));
        ratings.add(new CourseRating(new CourseRatingDto(2L,
                2L, 5, "Fair"), new Course(), new User()));
        ratings.add(new CourseRating(new CourseRatingDto(3L,
                3L, 6, "Average"), new Course(), new User()));

        int detractors = netPromoterScoreService.countDetractors(ratings);

        assertEquals(2, detractors);
    }

    @Test
    @DisplayName("Test countDetractors - Empty list")
    public void testCountDetractors_EmptyList() {

        List<CourseRating> ratings = new ArrayList<>();

        int detractors = netPromoterScoreService.countDetractors(ratings);

        assertEquals(0, detractors);
    }

    @Test
    @DisplayName("Test validateCourseForNpsCalculation - Course with < 4 enrollments")
    void testValidateCourseForNpsCalculation_WithLessThanFourEnrollments() {

        Course course = new Course();
        when(enrollmentRepository.countByCourse(course)).thenReturn(3);

        Exception exception = assertThrows(Exception.class, () -> netPromoterScoreService.validateCourseForNpsCalculation(course));
        assertEquals("Course has less than four enrollments.", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateAndSaveNpsForCourse - Course not found")
    void testCalculateAndSaveNpsForCourse_CourseNotFound() {

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> netPromoterScoreService.calculateAndSaveNpsForCourse(1L));
        assertEquals("Course not found with ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateAndSaveNpsForCourse - Course with < 4 enrollments")
    void testCalculateAndSaveNpsForCourse_WithLessThanFourEnrollments() {

        Course course = new Course();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse(course)).thenReturn(3);

        Exception exception = assertThrows(Exception.class, () -> netPromoterScoreService.calculateAndSaveNpsForCourse(1L));
        assertEquals("Course has less than four enrollments.", exception.getMessage());
    }

    @Test
    @DisplayName("Test calculateAndSaveNpsForCourse - NPS score already exists")
    void testCalculateAndSaveNpsForCourse_NpsScoreAlreadyExists() {

        Course course = new Course();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(enrollmentRepository.countByCourse(course)).thenReturn(5);
        when(netPromoterScoreRepository.findByCourseId(anyLong())).thenReturn(Optional.of(new NetPromoterScore()));

        Exception exception = assertThrows(Exception.class, () -> netPromoterScoreService.calculateAndSaveNpsForCourse(1L));
        assertEquals("NPS score already exists for course: 1", exception.getMessage());
    }



}
