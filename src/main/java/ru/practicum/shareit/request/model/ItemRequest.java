package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        ItemRequest other = (ItemRequest) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
