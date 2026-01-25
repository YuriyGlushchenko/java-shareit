package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ItemStorageImpl implements ItemStorage {
    private static final Map<Long, Item> storage = new HashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<Item> findById(long itemId) {
        return Optional.ofNullable(storage.get(itemId));
    }

    @Override
    public List<Item> search(String query) {
        return storage.values()
                .stream()
                .filter(item -> item.getAvailable() && (item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query)))
                .toList();
    }

    @Override
    public Item save(Item item) {

        Long newId = idGenerator.getAndIncrement();
        item.setId(newId);

        storage.put(newId, item);

        return item;
    }


    @Override
    public List<Item> getItemsByOwnerId(long ownerID) {
        return storage.values()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerID)
                .toList();
    }

    @Override
    public Item update(Item item) {
        storage.put(item.getId(), item);

        return item;
    }
}
