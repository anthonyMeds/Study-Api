package com.example.demo.controller;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.domain.users.User;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @Operation(summary = "Get an course data",
            description = "Get informations about courses, based in status.")
    @GetMapping("/getCourse")
    public ResponseEntity<Page<Course>> getUserData
            (
                    @Parameter(required = false, description = "status", example = "ACTIVE")
                    @RequestParam CourseStatus status,

                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "10") int size

            ) throws Exception {

        Page<Course> coursePage = courseService.getCourseByStatus(status, page, size);

        return new ResponseEntity<>(coursePage, HttpStatus.OK);
    }

}
