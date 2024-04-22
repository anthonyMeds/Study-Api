package com.example.demo.domain.netPromoterScore;

import com.example.demo.domain.courses.Course;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@Entity
@Table(name = "net_score_promoter_course")
public class NetPromoterScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @Column(name = "nps_score")
    private Float npsScore;

    @Column(name = "survey_date")
    private LocalDate surveyDate;

    @Column(name = "classification")
    private String classification;

    public NetPromoterScore(Course course, Float npsScore, String classification) {
        this.course = course;
        this.npsScore = npsScore;
        this.classification = classification;

    }

}
