package com.razvan.server.repositories;

import com.razvan.server.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User getUserByUserName(String username);
}
