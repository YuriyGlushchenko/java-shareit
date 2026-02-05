package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query("SELECT DISTINCT ir FROM ItemRequest ir " +
            "LEFT JOIN FETCH ir.requestor " +
            "LEFT JOIN FETCH ir.items " +
            "WHERE ir.requestor.id = :userId " +
            "ORDER BY ir.created DESC")
    List<ItemRequest> findByRequestorIdWithItems(@Param("userId") Long userId);

    List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long userId);

    @Query("SELECT ir FROM ItemRequest ir " +
            "LEFT JOIN FETCH ir.requestor " +
            "LEFT JOIN FETCH ir.items " +
            "WHERE ir.id = :requestId " +
            "ORDER BY ir.created DESC")
    Optional<ItemRequest> findByIdWithItems(@Param("requestId") Long requestId);
}
