package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
//    private final BookingService bookingService;
//
//    @PostMapping
//    public BookingDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
//                          @RequestBody @Valid NewBookingRequestDto newBookingDto) {
//        return bookingService.addNewBooking(userId, newBookingDto);
//    }
//
//    @PatchMapping("/{bookingId}")
//    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") Long userId,
//                              @PathVariable Long bookingId,
//                              @RequestParam(value = "approved") Boolean approved) {
//        return bookingService.approve(bookingId, userId, approved);
//    }
//
//    @GetMapping("/{bookingId}")
//    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
//                                     @PathVariable Long bookingId) {
//        return bookingService.getBookingById(userId, bookingId);
//    }
//
//    @GetMapping()
//    public List<BookingDto> getAllUserBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
//                                               @RequestParam(value = "state", defaultValue = "all") State state) {
//        return bookingService.getAllUserBookings(userId, state);
//    }
//
//    @GetMapping("/owner")
//    public List<BookingDto> getOwnerItemsBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
//                                                  @RequestParam(value = "state", defaultValue = "all") State state) {
//        return bookingService.getOwnerItemsBookings(userId, state);
//    }

}
