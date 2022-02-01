package com.agira.Agira.Repositories;

import com.agira.Agira.Entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    @Query("SELECT s FROM Schedule s WHERE s.schedule_id = ?1")
    public Schedule findById(int id);
}
