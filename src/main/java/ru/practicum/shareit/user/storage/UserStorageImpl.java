package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserStorageImpl implements UserStorage {
    private static final Map<Long, User> storage = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Long newId = idGenerator.getAndIncrement();
        user.setId(newId);

        storage.put(newId, user);

        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storage.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public User update(User user) {
        storage.put(user.getId(), user);

        return user;
    }


}
