package com.razvan.server.controller;

import com.razvan.server.model.Torrent;
import com.razvan.server.model.User;
import com.razvan.server.repositories.TorrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("files")
public class FileUploadController {
    private TorrentRepository repository;

    @Autowired
    public void setRepository(TorrentRepository repository) {
        this.repository = repository;
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        return factory.createMultipartConfig();
    }

    @GetMapping("/latest")
    public List<Torrent> getLatest() {
        return repository.findFirst3ByOrderByUploadTimeDesc();
    }

    @GetMapping("/pages")
    public long pages() {
        return repository.count() / 30 + (repository.count() % 30) > 0 ? 1 : 0;
    }

    @GetMapping("/{page}")
    public List<Torrent> getpage(@PathVariable("page") int page) {
        ArrayList<Torrent> torrents = new ArrayList<>(30);
        Iterator<Torrent> it = repository.findAll().iterator();
        page++;
        page *= 30;
        while (it.hasNext() && page > 0) {
            if(page <= 30)
                torrents.add(it.next());
            else
                it.next();
            page--;
        }
        return torrents;
    }

    @GetMapping("/byname")
    public List<Torrent> byName(@RequestParam(value = "name") String name,
                                @RequestParam(value = "page") int page) {
        return repository.getTorrentsByName(name).subList((page - 1) * 30, page * 30);
    }

    @GetMapping("/byname/pages")
    public int byNamePages(@RequestParam(value = "name") String name) {
        int count = repository.countTorrentByName(name);
        return count / 30 + count % 30 > 0 ? 1 : 0;
    }

    @GetMapping("/details")
    public Torrent details(@RequestParam(value = "id") long id) {
        return repository.getTorrentById(id);
    }

    @GetMapping("/details/rating")
    public float avgRating(@RequestParam(value = "id") long id) {
        return repository.getTorrentById(id).average();
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@RequestParam(value = "id") long id) {

        Torrent t = repository.getTorrentById(id);
        t.updateDownloads();
        repository.save(t);
        Resource file = new ClassPathResource(t.getPath());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public boolean handleFileUpload(@RequestParam(name = "file") MultipartFile file) {
        User u = UserController.getUser();
        if(u == null)
            return false;
        try {
            Path currentRelativePath = Paths.get("");
            File path = new File(currentRelativePath.toString() + "files" + u.getUserName());
            if (!path.mkdirs())
                return false;
            Torrent t = new Torrent(file.getName(),path.getPath(),u);
            File f = new File(t.getPath());
            Files.write(f.toPath(),file.getBytes());
            repository.save(t);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
