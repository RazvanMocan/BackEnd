package com.razvan.server.controller;

import com.razvan.server.model.Rating;
import com.razvan.server.model.Torrent;
import com.razvan.server.model.User;
import com.razvan.server.repositories.RatingRepository;
import com.razvan.server.repositories.TorrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RatingController {
    private RatingRepository repository;
    private TorrentRepository torrentRepository;

    @Autowired
    public void setRepository(RatingRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setTorrentRepository(TorrentRepository torrentRepository) {
        this.torrentRepository = torrentRepository;
    }

    @GetMapping("/rate")
    public void rate(@RequestParam(value = "id") long id,
                     @RequestParam(value = "mark") int mark) {
        User u = UserController.getUser();
        Torrent t = torrentRepository.getTorrentById(id);
        Rating r = new Rating(mark, u, t);
        repository.save(r);
    }
}
