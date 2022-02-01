package com.agira.Agira.Entities;
import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(name = "purifier_id", nullable = false)
    private int purifier_id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "date_of_birth", nullable = true)
    private Date date_of_birth;

    @Column(name = "gender", nullable = true)
    private String gender;

    @Column(name = "afflictions", nullable = true)
    private String afflictions;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPurifier_id() {
        return purifier_id;
    }

    public void setPurifier_id(int purifier_id) {
        this.purifier_id = purifier_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAfflictions() {
        return afflictions;
    }

    public void setAfflictions(String afflictions) {
        this.afflictions = afflictions;
    }
}