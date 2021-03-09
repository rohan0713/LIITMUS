package com.example.wiseapp;

public class UserhelperClass {

    String email , username , password , mobile;

    public UserhelperClass() {
    }

    public UserhelperClass(String email, String username, String password, String mobile) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
