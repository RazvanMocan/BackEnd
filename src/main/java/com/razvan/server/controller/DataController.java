package com.razvan.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    @GetMapping("/newest")
    public List<String> getNewest() {
        return Arrays.asList(
                "Object1",
                "Object2",
                "Object4"
        );
    }
}
