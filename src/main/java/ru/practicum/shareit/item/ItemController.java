package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.dto.UpdateItemRequestDTO;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDTO add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody @Valid NewItemRequestDTO item) {
        return itemService.addNewItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ItemDTO updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody UpdateItemRequestDTO item,
                              @PathVariable long itemId) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDTO getItem(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDTO> getOwnerItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDTO> searchItems(@RequestParam(value = "text", required = true) String query) {
        return itemService.searchItems(query);
    }


}
