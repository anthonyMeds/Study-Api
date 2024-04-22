package com.example.demo.controller;

import com.example.demo.domain.netPromoterScore.ResponseNetPromoterScore;
import com.example.demo.dto.NetPromoterScoreDto;
import com.example.demo.service.NetPromoterScoreService;
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
import java.util.Map;

@RestController
@RequestMapping("/nps")
public class NetPromoterScoreController {

    @Autowired
    private NetPromoterScoreService netPromoterScoreService;

    @Operation(summary = "Create a nps score",
            description = """
                    To create net promoter score for a course, inform : 
                    - course_id
                    """)
    @PostMapping("/create")
    public ResponseEntity<Map<Float, String>> createCourseRating
            (
                    @Valid
                    @RequestBody
                    NetPromoterScoreDto netPromoterScoreDto

            ) throws Exception {

        Map<Float, String> netPromoterScore = netPromoterScoreService
                .calculateAndSaveNpsForCourse(netPromoterScoreDto.courseId());

        return new ResponseEntity<>(netPromoterScore, HttpStatus.CREATED);

    }

    @Operation(summary = "Get the Net Promoter Score (NPS) from courses",
            description = "Get the net promoter score of all course.")
    @GetMapping("/getNps")
    public ResponseEntity<List<ResponseNetPromoterScore>> getAllNetPromoterScore() throws Exception {

        List<ResponseNetPromoterScore> netPromoterScoreList = netPromoterScoreService.getAllNetPromoterScores();

        return new ResponseEntity<>(netPromoterScoreList, HttpStatus.OK);
    }


}
