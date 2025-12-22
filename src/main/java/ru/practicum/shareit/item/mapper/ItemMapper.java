package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(NewItemRequestDTO request, User owner) {
        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .ownerId(owner.getId())
                .build();

        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }

    public static ItemDTO mapToItemDto(Item item) {
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setDescription(post.getDescription());
//        dto.setPostDate(post.getPostDate());
//
//        User author = post.getAuthor();
//        dto.setAuthor(UserMapper.mapToUserDto(author));
//
//        if (Objects.nonNull(post.getImages())) {
//            List<Long> imageIds = post.getImages()
//                    .stream()
//                    .map(Image::getId)
//                    .collect(Collectors.toList());
//            dto.setImages(imageIds);
//        }
        ItemDTO itemDTO = ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();

        return itemDTO;
    }
}