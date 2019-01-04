package com.razvan.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private User uploader;

    @JsonIgnore
    @OneToMany(mappedBy = "file")
    private List<Rating> ratings;

    private LocalDateTime uploadTime;
    @Transient
    private float avg;

    public Torrent(String name, String path, User uploader) {
        this.name = name;
        this.path = path;
        this.uploader = uploader;
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
        avg = 0;
        for (Rating rating : ratings)
            avg += rating.rating;
        avg /= ratings.size();
    }
}
