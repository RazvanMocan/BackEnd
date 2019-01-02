package com.razvan.server.repositories;

import com.razvan.server.model.Torrent;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TorrentRepository extends CrudRepository<Torrent, Long> {
    Torrent getTorrentById(Long id);
    List<Torrent> getTorrentsByName(String name);
    List<Torrent> findFirst3ByOrderByUploadTimeDesc();
    int countTorrentByName(String name);

}
