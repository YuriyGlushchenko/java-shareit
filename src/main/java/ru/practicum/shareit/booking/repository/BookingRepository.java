package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
