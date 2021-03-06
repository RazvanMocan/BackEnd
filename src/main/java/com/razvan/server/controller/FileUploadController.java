package com.razvan.server.controller;

import com.razvan.server.model.Torrent;
import com.razvan.server.model.User;
import com.razvan.server.repositories.TorrentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/")
public class FileUploadController {
    private TorrentRepository repository;
    private final static int pg = 12;

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

    private List<Torrent> update(List<Torrent> torrentList) {
        for (Torrent torrent : torrentList) {
            torrent.average();
        }
        return torrentList;
    }

    @GetMapping("/latest")
    public List<Torrent> getLatest() {
        return update(repository.findFirst3ByOrderByUploadTimeDesc());
    }

    @GetMapping("/pages")
    public long pages() {
        System.out.println(repository.count());
        System.out.println(repository.count()/pg);
        System.out.println((repository.count() % pg) > 0 ? 1 : 0);
        System.out.println(repository.count() / pg + ((repository.count() % pg) > 0 ? 1 : 0));
        return repository.count() / pg + ((repository.count() % pg) > 0 ? 1 : 0);
    }

    @GetMapping("/filenames")
    public List<String> getNames() {
        ArrayList<String> strings = new ArrayList<>();
        Iterable<Torrent> iterable =  repository.findAll();
        Iterator<Torrent> it = iterable.iterator();
        while (it.hasNext())
            strings.add(it.next().getName());
        return strings;
    }

    @GetMapping("/{page}")
    public List<Torrent> getpage(@PathVariable("page") int page) {
        ArrayList<Torrent> torrents = new ArrayList<>(pg);
        Iterable<Torrent> iterable = repository.findAll();
        Iterator<Torrent> it = iterable.iterator();
//        page++;
        page *= pg;
        while (it.hasNext() && page > 0) {
            if(page <= pg)
                torrents.add(it.next());
            else
                it.next();
            page--;
        }
        return update(torrents);

    }

    @GetMapping("/byname")
    public List<Torrent> byName(@RequestParam(value = "name") String name,
                                @RequestParam(value = "page") int page) {
        int count = repository.countTorrentByName(name);
        if(page * pg < count)
            count = page * pg;

        return update(repository.getTorrentsByName(name).subList((page - 1) * pg, count));
    }

    @GetMapping("/byname/pages")
    public int byNamePages(@RequestParam(value = "name") String name) {
        int count = repository.countTorrentByName(name);
        return count / pg + count % pg > 0 ? 1 : 0;
    }

    @GetMapping("/details")
    public Torrent details(@RequestParam(value = "id") long id) {
        Torrent torrent = repository.getTorrentById(id);
        torrent.average();
        return torrent;
    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<InputStreamResource> serveFile(@RequestParam(value = "id") long id) {
        if (UserController.getUser().isBanned())
            return null;
        Torrent t = repository.getTorrentById(id);
        t.updateDownloads();
        repository.save(t);
        File d = new File(t.getPath());
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(d));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + d.getName() + "\"").body(resource);
    }

    @PostMapping("/")
    public boolean handleFileUpload(@RequestParam(name = "file") MultipartFile file) {
        User u = UserController.getUser();
        if(u == null || u.isBanned())
            return false;
        Path currentRelativePath = Paths.get("");
        File path = new File(currentRelativePath.toAbsolutePath().toString() + "/files/" + u.getUserName());
        Torrent t = new Torrent(file.getOriginalFilename(),path.getPath(),u);

        t = repository.save(t);
        File f = new File(t.getPath());
        if(!f.getParentFile().exists() && !f.getParentFile().mkdirs())
            return error(t);
        System.out.println(t.getUploadTime());
        System.out.println(f.getAbsolutePath());

        try {
            if (!f.exists() && !f.createNewFile())
                return error(t);
            Files.write(f.toPath(),file.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return error(t);
        }
    }

    private boolean error(Torrent t) {
        repository.delete(t);
        return false;
    }
}
