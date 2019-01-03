package com.razvan.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "file")
    private List<Rating> ratings;

    private LocalDateTime uploadTime;

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

    public float average() {
        float avg = 0;
        for (Rating rating : ratings)
            avg += rating.rating;
        return avg / ratings.size();
    }
}
