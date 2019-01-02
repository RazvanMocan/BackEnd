package com.razvan.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private Long id;

    int rating;

    @ManyToOne
    private User reviewer;

    @ManyToOne
    private Torrent file;

    public Rating(int rating, User reviewer, Torrent file) {
        this.rating = rating;
        this.reviewer = reviewer;
        this.file = file;
    }
}
