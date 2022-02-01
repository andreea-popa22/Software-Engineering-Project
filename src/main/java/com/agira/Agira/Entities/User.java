package com.agira.Agira.Entities;
import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        if(user_id != user.user_id){
            System.out.println("1");
        }
        if(purifier_id != user.purifier_id){
            System.out.println("2");
        }
        if(!Objects.equals(username, user.username)){
            System.out.println("3");
        }
        if(!Objects.equals(password, user.password)){
            System.out.println("4");
        }
        if(!(date_of_birth.compareTo(user.date_of_birth)==0)){
            System.out.println(date_of_birth.compareTo(user.date_of_birth));
            System.out.println(date_of_birth);
            System.out.println(user.date_of_birth);
            System.out.println("5");
        }
        if(!Objects.equals(afflictions, user.afflictions)){
            System.out.println("6");
        }
        if(!Objects.equals(gender, user.gender)){
            System.out.println("7");
        }


        return Objects.equals(username, user.username) && Objects.equals(gender, user.gender) && Objects.equals(afflictions, user.afflictions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, purifier_id, username, password, date_of_birth, gender, afflictions);
    }

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