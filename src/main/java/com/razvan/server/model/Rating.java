package com.razvan.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    @ManyToOne
    private User reviewer;

    @ManyToOne
    private Torrent file;

    public Rating(int rating, User reviewer, Torrent file) {
        this.rating = rating;
        this.reviewer = reviewer;
        this.file = file;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean checkUser(User u) {
        if(u == null)
            return false;
        return reviewer.getUserName().equals(u.getUserName());
    }
}
