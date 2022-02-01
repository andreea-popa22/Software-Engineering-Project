package com.agira.Agira.Repositories;

import com.agira.Agira.Entities.Purifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurifierRepository extends JpaRepository<Purifier, Long>{
    @Query("SELECT p FROM Purifier p WHERE p.purifier_id = ?1")
    public Purifier findById(int id);
}
