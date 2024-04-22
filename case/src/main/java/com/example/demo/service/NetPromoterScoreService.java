package com.example.demo.service;

import com.example.demo.domain.courseRating.CourseRating;
import com.example.demo.domain.courses.Course;
import com.example.demo.domain.netPromoterScore.NetPromoterScore;
import com.example.demo.domain.netPromoterScore.ResponseNetPromoterScore;
import com.example.demo.repository.CourseRatingRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.NetPromoterScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class NetPromoterScoreService {

    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private NetPromoterScoreRepository netPromoterScoreRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public Map<Float, String> calculateAndSaveNpsForCourse(Long courseId) throws Exception {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("Course not found with ID: " + courseId));

        validateCourseForNpsCalculation(course);

        List<CourseRating> ratings = getRatingsForCourse(courseId)
                .orElseThrow(() -> new Exception("Failed to get ratings for course: " + courseId));

        int promoters = countPromoters(ratings);
        int detractors = countDetractors(ratings);

        float nps = calculateNps(promoters, detractors, ratings.size());

        String classificationOfNps = classifyNPS(nps);

        saveNpsForCourse(course, nps, classificationOfNps);


        Map<Float, String> npsClassificationMap = new HashMap<>();
        npsClassificationMap.put(nps, classificationOfNps);

        return npsClassificationMap;
    }

    private void validateCourseForNpsCalculation(Course course) throws Exception {

        if (enrollmentRepository.countByCourse(course) < 4) {
            throw new Exception("Course has less than four enrollments.");
        }
    }

    private Optional<List<CourseRating>> getRatingsForCourse(Long courseId) {
        List<CourseRating> ratings = courseRatingRepository.findByCourseId(courseId);
        return Optional.ofNullable(ratings);
    }

    private int countPromoters(List<CourseRating> ratings) {
        return (int) ratings.stream()
                .filter(Objects::nonNull)
                .filter(rating -> rating.getRating() >= 9)
                .count();
    }

    private int countDetractors(List<CourseRating> ratings) {
        return (int) ratings.stream()
                .filter(Objects::nonNull)
                .filter(rating -> rating.getRating() < 6)
                .count();
    }

    private float calculateNps(int promoters, int detractors, int totalRespondents) throws Exception {

        if (totalRespondents == 0) {
            throw new Exception("The course has no rating yet.");
        }
        return (promoters - detractors) / (float) totalRespondents * 100;
    }


    private void saveNpsForCourse(Course course, float nps, String classificationOfNps) {
        NetPromoterScore netPromoterScore = new NetPromoterScore();
        netPromoterScore.setCourse(course);
        netPromoterScore.setNpsScore(nps);
        netPromoterScore.setClassification(classificationOfNps);
        netPromoterScore.setSurveyDate(LocalDate.now());

        netPromoterScoreRepository.save(netPromoterScore);
    }

    public String classifyNPS(float nps) {
        if (nps >= 75) {
            return "Excellent";
        } else if (nps >= 50) {
            return "Very good";
        } else if (nps >= 0) {
            return "Reasonable";
        } else {
            return "Bad";
        }
    }

    public List<ResponseNetPromoterScore> getAllNetPromoterScores() throws Exception {
        List<NetPromoterScore> netPromoterScoreList = netPromoterScoreRepository.findAll();

        if (netPromoterScoreList.isEmpty()) {
            throw new Exception("Net Promoter Score not found.");
        }
        List<ResponseNetPromoterScore> responseList = new ArrayList<>();
        for (NetPromoterScore nps : netPromoterScoreList) {

            ResponseNetPromoterScore dto = mapToResponseDto(nps);

            responseList.add(dto);
        }

        return responseList;
    }

    private ResponseNetPromoterScore mapToResponseDto(NetPromoterScore netPromoterScore) {
        return new ResponseNetPromoterScore(
                netPromoterScore.getNpsScore(),
                netPromoterScore.getClassification(),
                netPromoterScore.getCourse().getId() ,
                netPromoterScore.getCourse().getName()
        );
    }


}
