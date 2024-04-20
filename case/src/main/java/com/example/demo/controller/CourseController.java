package com.example.demo.controller;

import com.example.demo.domain.courses.Course;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.service.CourseService;
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
@RequestMapping(value = "/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Operation(summary = "Create a course",
            description = """
                    
                    To create an course it is requested to inform a valid : 
                    - Name
                    - Code
                    - Instructor
                    - Description
                    - Status
                    - Creation Date
                    
                    """)
    @PostMapping("/create")
    public ResponseEntity<Course> createUser
            (
                    @Valid
                    @RequestBody
                    CourseDto courseDto

            ) throws Exception {

        Course newCourse = courseService.createCourse(courseDto);

        return new ResponseEntity<>(newCourse, HttpStatus.CREATED);

    }

}
