package com.agira.Agira.Entities;

import javax.persistence.*;

@Entity
@Table(name = "AUDIO")
public class Audio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int audio_id;

    @Column(name = "audio_file", nullable = false)
    private String audio_file;

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }
}