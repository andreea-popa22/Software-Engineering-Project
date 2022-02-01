package com.agira.Agira.Repositories;

import com.agira.Agira.Entities.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AudioRepository extends JpaRepository<Audio, Long> {
    @Query("SELECT a FROM Audio a WHERE a.audio_id = ?1")
    public Audio findById(int id);
}
