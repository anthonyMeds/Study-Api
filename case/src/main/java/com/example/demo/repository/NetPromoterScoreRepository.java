package com.example.demo.repository;

import com.example.demo.domain.netPromoterScore.NetPromoterScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NetPromoterScoreRepository extends JpaRepository<NetPromoterScore, Long> {
    Optional<NetPromoterScore> findByCourseId(Long courseId);
}
