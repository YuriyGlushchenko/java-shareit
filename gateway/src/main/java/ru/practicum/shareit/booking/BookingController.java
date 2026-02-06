package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.model.State;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody @Valid NewBookingRequestDto newBookingDto) {
        log.info("Creating booking {}, userId={}", newBookingDto, userId);
        return bookingClient.add(userId, newBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam(value = "approved") Boolean isApproved) {
        return bookingClient.approve(bookingId, userId, isApproved);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(value = "state", defaultValue = "all") State state) {

        log.info("Get all user booking with state {}, userId={}", state, userId);
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(value = "state", defaultValue = "all") State state) {
        log.info("Get owner bookings, userId={}", userId);
        return bookingClient.getOwnerItemsBookings(userId, state);
    }
}
