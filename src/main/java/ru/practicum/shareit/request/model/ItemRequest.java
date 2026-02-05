package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */


@Entity
@Table(name = "item_requests")
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)  //  безопасно для @OneToMany и lazy-загрузок
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA обязан иметь конструктор без аргументов
@AllArgsConstructor  // для Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Column(name = "description", nullable = false)
    @ToString.Include
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    @ToString.Exclude
    private User requestor;

    @Column(name = "created", nullable = false)
    @ToString.Include
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();

    // bidirectional для получения списка itemRequest сразу с items одним запросом с JOIN FETCH (без N+1)
    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
        item.setRequest(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setRequest(null);
    }

}
