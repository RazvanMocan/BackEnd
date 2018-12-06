package com.razvan.server.model;

import lombok.Data;

@Data
public class User {
    private String userName, password;
    private boolean admin;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public void login() throws SecurityException {
//        throw new SecurityException("Login failed. Wrong username or password ");
    }

    public void siggup() {

    }

    public void rateFile() {

    }
}
