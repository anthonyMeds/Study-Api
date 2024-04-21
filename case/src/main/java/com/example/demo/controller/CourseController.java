package com.example.demo.controller;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.courses.CourseStatus;
import com.example.demo.dto.course.CourseDto;
import com.example.demo.dto.course.ResponseCourseDto;
import com.example.demo.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "Get a course data",
            description = "Get informations about courses, based in status.")
    @GetMapping("/getCourse")
    public ResponseEntity<Page<ResponseCourseDto>> getUserData
            (
                    @Parameter(required = false, description = "status", example = "ACTIVE")
                    @RequestParam @NotNull CourseStatus status,

                    @RequestParam(name = "page", defaultValue = "0") int page,
                    @RequestParam(name = "size", defaultValue = "10") int size

            ) {

        Page<ResponseCourseDto> coursePage = courseService.getCourseByStatus(status, page, size);

        return new ResponseEntity<>(coursePage, HttpStatus.OK);
    }

    @Operation(summary = "Update course status",
            description = "To activate or inactivate a course, inform : course code and desired status. ")
    @PutMapping("/updateCourseStatus")
    public ResponseEntity<ResponseCourseDto> updateCourseStatus
            (
                    @Parameter(description = "course code", example = "java-boot")
                    @RequestParam @NotBlank String courseCode ,
                    @Parameter(description = "course status", example = "ACTIVE")
                    CourseStatus courseStatus

             ) throws Exception {

        ResponseCourseDto course = courseService.updateCourseStatus(courseCode, courseStatus);

        return new ResponseEntity<>(course, HttpStatus.OK);

    }

    @Operation(summary = "Get all courses",
            description = "Internal api to check all courses.")
    @GetMapping("/getAll")
    public ResponseEntity<List<Course>> getAllCourses() throws Exception {
        List<Course> courses = courseService.getAllCourses();

        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


}
