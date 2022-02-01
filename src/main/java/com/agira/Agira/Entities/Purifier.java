package com.agira.Agira.Entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "PURIFIER")
public class Purifier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purifier_id;

    @Column(name = "audio_id", nullable = true)
    private int audio_id;

    @Column(name = "message_id", nullable = true)
    private int message_id;

    @Column(name = "collection_id", nullable = true)
    private int collection_id;

    @Column(name = "hex", nullable = true)
    private String hex;

    @Column(name = "is_on", nullable = false)
    private boolean is_on;

    @Column(name = "location_name", nullable = true)
    private String location_name;

    @Column(name = "schedule_id", nullable = true)
    private int schedule_id;

    public int getPurifier_id() {
        return purifier_id;
    }

    public void setPurifier_id(int purifier_id) {
        this.purifier_id = purifier_id;
    }

    public int getAudio_id() {
        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public int getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(int collection_id) {
        this.collection_id = collection_id;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public boolean is_on() {
        return is_on;
    }

    public void setIs_on(boolean is_on) {
        this.is_on = is_on;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }
}
