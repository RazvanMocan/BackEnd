package com.razvan.server.controller;

import com.razvan.server.model.User;
import com.razvan.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private static User user;
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("user/login/{userID}")
    public User login(
            @PathVariable("userID") String uname,
            @RequestParam(value="password") String pass) {

        User user = repository.getUserByUserName(uname);
                System.out.println(uname+" "+pass);

        if (user.login(pass))
            UserController.user = user;
        return user;
    }

    @GetMapping("user/logout")
    public void logout() {
        user = null;
    }

    @PostMapping("user/signup/{userID}")
    public User signup(
            @PathVariable("userID") String uname,
            @RequestParam(value="password") String pass ) {

        User user = repository.getUserByUserName(uname);
        if (user == null)
            return null;

        UserController.user = user;
        user = new User(uname, pass);
        repository.save(user);
        return user;
    }

    @PostMapping("user/ban/{userID}")
    public void signup(
            @PathVariable("userID") String uname) {

        if (loggedin() && user.isAdmin()) {
            User banned = repository.getUserByUserName(uname);
            banned.setBanned(true);
            repository.save(banned);
        }
    }

    private static boolean loggedin() {
        return user != null;
    }

    protected static User getUser() {
        return user;
    }
}
