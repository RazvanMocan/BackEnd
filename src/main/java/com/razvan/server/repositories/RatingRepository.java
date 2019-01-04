package com.razvan.server.repositories;

import com.razvan.server.model.Rating;
import com.razvan.server.model.Torrent;
import com.razvan.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    Rating getRatingByFileAndReviewer(Torrent t, User u);
}
