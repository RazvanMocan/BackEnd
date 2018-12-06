package com.razvan.server.controller;

import com.razvan.server.model.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private User user;

    @GetMapping(value = "user/login/{userID}")
    public User login(
            @PathVariable("userID") String id,
            @RequestParam(value="password") String pass) {

        User user = new User(id, pass);
        System.out.println(id+" "+pass);
        try {
            user.login();
            System.out.println("perfect");
        } catch (SecurityException e) {
            System.out.println("De ce?");
            return null;
        }

        this.user = user;
        return user;
    }

}
