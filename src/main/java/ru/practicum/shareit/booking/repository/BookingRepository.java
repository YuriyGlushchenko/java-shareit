package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long bookerId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(
            Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(
            Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(
            Long bookerId, Status status);


    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime now1, LocalDateTime now2);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(
            Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(
            Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(
            Long ownerId, Status status);


    Booking findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(
            Long itemId,
            Status status,
            LocalDateTime now
    );

    Booking findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
            Long itemId,
            Status status,
            LocalDateTime now
    );

    boolean existsByBookerIdAndItemIdAndStatusAndEndBefore(
            Long bookerId,
            Long itemId,
            Status status,
            LocalDateTime now
    );

    boolean existsByStartBetweenOrEndBetween(
            LocalDateTime start,
            LocalDateTime end,
            LocalDateTime start1,
            LocalDateTime end1

    );

    @Query("""
                SELECT COUNT(b) > 0
                FROM Booking b
                WHERE b.item.id = :itemId
                AND b.status IN (:statuses)
                AND (
                    (b.start < :end AND b.end > :start) OR
                    b.start = :start OR
                    b.end = :end
                )
            """)
    boolean existsOverlappingBooking(
            @Param("itemId") Long itemId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("statuses") List<Status> statuses
    );

    List<Booking> findByItemIdAndStatusOrderByStartAsc(Long itemId, Status status);


    @Query("""
                SELECT b
                FROM Booking b
                WHERE b.item.id IN :itemIds
                AND b.status = :status
                ORDER BY b.item.id, b.start ASC
            """)
    List<Booking> findAllBookingsForItems(
            @Param("itemIds") List<Long> itemIds,
            @Param("status") Status status
    );


}
