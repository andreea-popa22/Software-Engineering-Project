package com.agira.Agira.Repositories;

import com.agira.Agira.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.message_id = ?1")
    public Message findById(int id);
}
