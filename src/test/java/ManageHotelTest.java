import entities.Booking;
import entities.Hotel;
import entities.Room;
import exceptions.BookingNotFoundException;
import exceptions.ParamNotValidException;
import exceptions.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManageHotelTest {

    private String NEW_CUSTOMER_FULLNAME;
    private Room ROOM;
    private Hotel HOTEL;
    private Booking BOOKING;
    private ManageHotel MANAGE_HOTEL;

    @BeforeEach
    public void setUp() {
        NEW_CUSTOMER_FULLNAME = "Dupont Bernard";
        ROOM = Room.builder().roomNumber(1).capacity(2).price(70).build();
        BOOKING = Booking.builder().reference(0).roomNumber(1).fullName("John Doe").checkInDate(LocalDate.now().plusMonths(2)).checkOutDate(LocalDate.now().plusMonths(2).plusDays(3)).build();
        HOTEL = Hotel.builder()
                .rooms(List.of(ROOM))
                .bookings(new ArrayList<>(Collections.singletonList(BOOKING))).build();
        MANAGE_HOTEL = ManageHotel.builder()
                .hotel(HOTEL)
                .build();
    }


    /**
     * Test for searchBooking method.
     */

    @Test
    public void should_throw_exception_when_search_with_not_valid_param() {
        // the cas if full name is null
        assertThrows(ParamNotValidException.class, () -> MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), null, BOOKING.getCheckInDate()));
        // the cas if date is null
        assertThrows(ParamNotValidException.class, () -> MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), null));
        // the cas if full name is empty
        assertThrows(ParamNotValidException.class, () -> MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), "", BOOKING.getCheckInDate()));
    }

    @Test
    public void should_throw_exception_when_search_with_not_exist_room() {
        // the cas if room not exist
        assertThrows(RoomNotFoundException.class, () -> MANAGE_HOTEL.searchBooking(2, BOOKING.getFullName(), BOOKING.getCheckInDate()));
    }

    @Test
    public void should_throw_exception_when_search_with_not_found_booking() {
        // the cas if booking with full name not exist
        assertThrows(BookingNotFoundException.class, () -> MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), "not exist", BOOKING.getCheckInDate()));
        // the cas if booking with date not exist
        assertThrows(BookingNotFoundException.class, () -> MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), LocalDate.now()));
    }

    @Test
    public void should_return_booking_when_search() throws BookingNotFoundException, ParamNotValidException {
        // the cas if BOOKING with date equals checkInDate
        Booking result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckInDate());
        assertEquals(BOOKING, result);
        // the cas if BOOKING with date equals checkOutDate
        result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckOutDate());
        assertEquals(BOOKING, result);
        // the cas if BOOKING with date between checkInDate and checkOutDate
        result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckInDate().plusDays(1));
        assertEquals(BOOKING, result);
    }
}