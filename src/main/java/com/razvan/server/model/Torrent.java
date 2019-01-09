package com.razvan.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.razvan.server.controller.UserController;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Torrent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int downloads;

    private String path;
    @ManyToOne()
    @JsonIgnore
    private User uploade;

    @Transient
    private String uploader;

    public String getName() {
        return name;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "file")
    private List<Rating> ratings;

    private LocalDateTime uploadTime;
    @Transient
    private float rating;

    public Torrent(String name, String path, User uploader) {
        this.name = name;
        this.path = path;
        this.uploade = uploader;
        downloads = 0;
        uploadTime = LocalDateTime.now();
    }

    public void updateDownloads() {
        downloads++;
    }

    public String getPath() {
        return path + "/" + id + name;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void average() {
        this.uploader = this.uploade.getUserName();
        for (Rating rating1 : ratings)
            if (rating1.checkUser(UserController.getUser())) {
                rating = rating1.getRating();
                return;
            }

        rating = 0;
        for (Rating rating : ratings)
            this.rating += rating.getRating();
        rating /= ratings.size();
    }
}
