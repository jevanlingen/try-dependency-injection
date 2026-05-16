package com.di.exampleapp.repository;

import com.di.annotations.Repository;
import com.di.model.User;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>(List.of(new User(1, "Jacob")));

    public List<User> findAll() {
        return users;
    }

    public User findById(int id) {
        return users.stream()
                .filter(user -> user.id() == id)
                .findFirst()
                .orElse(null);
    }

    public User save(User user) {
        final var user_ = new User(users.getLast().id() + 1, user.name());
        users.add(user_);
        return user_;
    }
}
