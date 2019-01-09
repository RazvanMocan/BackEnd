package com.razvan.server.controller;

import com.razvan.server.model.User;
import com.razvan.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("/api")
public class UserController {
    private User user;
    private static User user2;
    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
        User u = new User("admin", "admin");
        u.setAdmin(true);
        repository.save(u);
    }

    @GetMapping("user/login/{userID}")
    public User login(
            @PathVariable("userID") String uname,
            @RequestParam(value="password") String pass) {

        User user2 = repository.getUserByUserName(uname);
        System.out.println(uname+" "+pass);

        if (user2 == null)
            return null;

        System.out.println(user2.getPassword());
        System.out.println(user2.login(pass));
        if (!user2.login(pass))
            return null;
        this.user = user2;
        UserController.user2 = user2;
        return user2;
    }

    @GetMapping("user/logout")
    public void logout() {
        this.user = null;
        UserController.user2 = null;
        System.out.println("logout");
        System.out.println(user == null);
    }

    @PostMapping("user/signup/{userID}")
    public User signup(
            @PathVariable("userID") String uname,
            @RequestParam(value="password") String pass ) {

        User user = repository.getUserByUserName(uname);
        if (user != null)
            return null;

        user = new User(uname, pass);
        repository.save(user);
        return user;
    }

    @GetMapping("user/ban")
    public void ban(@RequestParam("userID") String uname) {
        System.out.println(uname);
        if (loggedin() && user.isAdmin()) {
            User banned = repository.getUserByUserName(uname);
            banned.setBanned(!banned.isBanned());
            repository.save(banned);
        }
    }

    private boolean loggedin() {
        return user != null;
    }

    @GetMapping("/loggedin")
    public static User getUser() {
        return UserController.user2;
    }

    @GetMapping("/all")
    public List<User> getUsers() {
        ArrayList<User> users = new ArrayList<>();
        Iterable<User> iterable =  repository.findAll();
        Iterator<User> it = iterable.iterator();
        while (it.hasNext())
            users.add(it.next());
        return users;
    }

    @GetMapping("/banned")
    public boolean isbanned() {
        if (user == null)
            return false;
        return user.isBanned();
    }
}
