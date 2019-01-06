package com.razvan.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
public class User {
    @Id
    private String userName;

    public boolean isBanned() {
        return isBanned;
    }

    @JsonIgnore
    private  String password;

    public String getUserName() {
        return userName;
    }

    private boolean admin = false, isBanned = false;

    @JsonIgnore
    @OneToMany(mappedBy = "uploade")
    private List<Torrent> torrents;

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "reviewer")
    private List<Rating> ratings;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    public boolean login(String pass) {
        return password.equals(pass);
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isAdmin() {
        return admin;
    }
}
