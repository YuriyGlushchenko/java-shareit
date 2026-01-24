package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findById(Long userId);

    List<User> findAll();

    User save(User user);

    Optional<User> findByEmail(String email);

    User update(User user);

    void delete(long id);
}
