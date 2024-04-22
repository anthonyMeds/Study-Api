package com.example.demo.domain.courseRating;

import com.example.demo.domain.courses.Course;
import com.example.demo.domain.users.User;
import com.example.demo.dto.CourseRatingDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
@Entity
@Table(name = "course_rating")
public class CourseRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "rating")
    private int rating;

    @Column(name = "feedback", length = 255)
    private String feedback;

    @Column(name = "rating_date")
    private LocalDateTime ratingDate;

    public CourseRating(CourseRatingDto courseRatingDto, Course course, User user) {
        this.course = course;
        this.user = user;
        this.rating = courseRatingDto.rating();
        this.feedback = courseRatingDto.feedback();
        this.ratingDate = LocalDateTime.now();
    }

}
