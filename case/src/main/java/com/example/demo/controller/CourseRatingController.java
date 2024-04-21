package com.example.demo.controller;

import com.example.demo.domain.courseRating.CourseRatingDto;
import com.example.demo.domain.courseRating.ResponseCourseRatingDto;
import com.example.demo.service.CourseRatingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/courseRating")
public class CourseRatingController {


    @Autowired
    private CourseRatingService courseRatingService;


    @Operation(summary = "Create a course rating",
            description = """
                    To create course rating, inform : 
                    - user_id
                    - course_id
                    - rating
                    - feedack
                    """)
    @PostMapping("/create")
    public ResponseEntity<ResponseCourseRatingDto> createCourseRating
            (
                    @Valid
                    @RequestBody
                    CourseRatingDto courseRatingDto

            ) throws Exception {

        ResponseCourseRatingDto courseRating = courseRatingService.createCourseRating(courseRatingDto);

        return new ResponseEntity<>(courseRating, HttpStatus.CREATED);

    }

}
