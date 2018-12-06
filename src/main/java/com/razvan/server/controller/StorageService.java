package com.razvan.server.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

public class StorageService {
    void init() {

    }

    void store(MultipartFile file) {
    }

    Stream<Path> loadAll() {
        ArrayList<Path> paths = new ArrayList<>();
        try {
            paths.add(new ClassPathResource("application.yaml").getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths.stream();
    }

    Path load(String filename) {
        return null;
    }

    Resource loadAsResource(String filename) {
        return new ClassPathResource(filename);
    }

    void deleteAll() {

    }
}
