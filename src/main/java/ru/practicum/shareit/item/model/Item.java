package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "items")
@Builder
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)  //  безопасно для @OneToMany и lazy-загрузок
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA обязан иметь конструктор без аргументов
@AllArgsConstructor  // для Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude  // Чтобы избежать случайной lazy-загрузки
    private User owner;

    @Column(name = "name", nullable = false)
    @ToString.Include
    private String name;

    @Column(name = "description")
    @ToString.Include
    private String description;

    @Builder.Default
    @Column(name = "available", nullable = false)
    @ToString.Include
    private Boolean available = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    @ToString.Exclude  // Чтобы избежать случайной lazy-загрузки
    private ItemRequest request;

    public void setRequest(ItemRequest request) {
        this.request = request;
        if (request != null && !request.getItems().contains(this)) {
            request.getItems().add(this);
        }
    }

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        Item other = (Item) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
