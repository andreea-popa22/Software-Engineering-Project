package com.agira.Agira.Entities;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "STATISTICS")
public class Statistics {
    @Id
    @Column(name="statistic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statistic_id;

    @Column(name="purifier_id")
    private int purifier_id;

    @Column(name = "timestamp")
    private Time timestamp;

    @Column(name = "co")
    private String co;

    @Column(name = "no2")
    private String no2;

    @Column(name = "ozone")
    private String ozone;

    @Column(name = "so2")
    private String so2;

    public int getStatistic_id() {
        return statistic_id;
    }

    public void setStatistic_id(int statistic_id) {
        this.statistic_id = statistic_id;
    }

    public int getPurifier_id() {
        return purifier_id;
    }

    public void setPurifier_id(int purifier_id) {
        this.purifier_id = purifier_id;
    }

    public Time getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Time timestamp) {
        this.timestamp = timestamp;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getNo2() {
        return no2;
    }

    public void setNo2(String no2) {
        this.no2 = no2;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public String getSo2() {
        return so2;
    }

    public void setSo2(String so2) {
        this.so2 = so2;
    }
}
