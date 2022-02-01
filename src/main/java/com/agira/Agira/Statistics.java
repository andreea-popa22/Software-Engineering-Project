package com.agira.Agira;

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

    @Column(name = "dust")
    private String dust;

    @Column(name = "ozone")
    private String ozone;

    @Column(name = "humidity")
    private String humidity;

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

    public String getDust() {
        return dust;
    }

    public void setDust(String dust) {
        this.dust = dust;
    }

    public String getOzone() {
        return ozone;
    }

    public void setOzone(String ozone) {
        this.ozone = ozone;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
