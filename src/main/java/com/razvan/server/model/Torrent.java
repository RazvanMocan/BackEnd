package com.razvan.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Torrent {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Torrent(String name, String path, User uploader) {
        this.name = name;
        this.path = path;
        this.uploader = uploader;
        downloads = 0;
    }

    private int downloads;
    private String path;

    @ManyToOne()
    private User uploader;

    @OneToMany(mappedBy = "file")
    private List<Rating> ratings;

    @CreatedDate
    private LocalDateTime uploadTime;

    public void updateDownloads() {
        downloads++;
    }

    public String getPath() {
        return path + id + name;
    }

    public float average() {
        float avg = 0;
        for (Rating rating : ratings)
            avg += rating.rating;
        return avg / ratings.size();
    }
}
