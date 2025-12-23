package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item save(Item item);

    Optional<Item> findById(long itemId);

    Item update(Item item);

    int getSharingCount(long itemID);

    List<Item> getItemsByOwnerId(long ownerID);

    List<Item> search(String query);

}
