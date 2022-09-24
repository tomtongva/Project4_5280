package com.group3.project4;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    public static String FEMALE = "FEMALE";
    public static String MALE = "MALE";

    String email, username, first_name, last_name, city, gender, image_location, id;
    Boolean is_deleted;

    public User() {
        // empty constructor
    }

    public User(String id, String email, String first_name, String last_name, String city, String gender, String image_location) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.gender = gender;

        username = String.valueOf(first_name.toLowerCase().charAt(0)) + last_name.toLowerCase();
        this.image_location = image_location;
        is_deleted = false;
    }

    public User(String email, String username, String first_name, String last_name, String city,
                String gender, String image_location, Boolean is_deleted) {
        this.email = email;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.gender = gender;
        this.image_location = image_location;
        this.is_deleted = is_deleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage_location() {
        return image_location;
    }

    public void setImage_location(String image_location) {
        this.image_location = image_location;
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email) && username.equals(user.username) && first_name.equals(user.first_name) && last_name.equals(user.last_name) && city.equals(user.city) && gender.equals(user.gender) && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username, first_name, last_name, city, gender, id);
    }
}
