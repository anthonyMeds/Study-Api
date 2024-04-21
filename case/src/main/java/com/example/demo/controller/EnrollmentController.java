package com.example.demo.controller;

import com.example.demo.domain.enrollment.Enrollment;
import com.example.demo.domain.enrollment.ResponseEnrollmentDto;
import com.example.demo.dto.enrollment.EnrollmentDto;
import com.example.demo.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/enrollment")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;


    @Operation(summary = "Enroll an user to a course",
            description = """
                    To create registration inform : 
                    - user_id
                    - course_id
                    """)
    @PostMapping("/create")
    public ResponseEntity<ResponseEnrollmentDto> createRegistration
            (
                    @Valid
                    @RequestBody
                    EnrollmentDto enrollmentDto

            ) throws Exception {

        ResponseEnrollmentDto enrollment = enrollmentService.createRegistration(enrollmentDto);

        return new ResponseEntity<>(enrollment, HttpStatus.CREATED);

    }

    @Operation(summary = "Get all enrollments",
            description = "Internal api to check all enrollments.")
    @GetMapping("/getAll")
    public ResponseEntity<List<Enrollment>> getAllEnrollment() throws Exception {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollment();

        return new ResponseEntity<>(enrollments, HttpStatus.OK);
    }

}
