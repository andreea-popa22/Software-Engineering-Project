package com.agira.Agira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StatisticsRepository extends JpaRepository<Statistics, Integer> {
    @Query("SELECT s FROM Statistics s WHERE s.statistic_id = ?1")
    public Statistics findById(int id);
}
