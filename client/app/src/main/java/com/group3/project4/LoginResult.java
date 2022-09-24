package com.group3.project4;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
