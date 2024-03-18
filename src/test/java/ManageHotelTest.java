import entities.Booking;
import entities.Room;
import exceptions.BookingNotFoundException;
import exceptions.ParamNotValidException;
import exceptions.RoomNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManageHotelTest {

    private String NEW_CUSTOMER_FULLNAME;
    private Room ROOM;
    private Booking BOOKING;
    private ManageHotel MANAGE_HOTEL;

    @BeforeEach
    public void setUp() {
        NEW_CUSTOMER_FULLNAME = "Dupont Bernard";
        ROOM = Room.builder().roomNumber(1).capacity(2).price(70).build();
        BOOKING = Booking.builder().reference(0).roomNumber(1).fullName("John Doe").checkInDate(LocalDate.now().plusMonths(2)).checkOutDate(LocalDate.now().plusMonths(2).plusDays(3)).build();
        MANAGE_HOTEL = ManageHotel.builder()
                .rooms(new ArrayList<>(Collections.singletonList(ROOM)))
                .bookings(new ArrayList<>(Collections.singletonList(BOOKING)))
                .build();
    }

    /**
     * Test for checkRoomAvailability method.
     */

    @Test
    void should_return_false_when_room_is_not_available() {
        // the cas if checkIn is before checkInDate and checkOut is after checkInDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckInDate().plusDays(1)));
        // the cas if checkIn is before checkOutDate and checkOut is after checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate().minusDays(1), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is before checkInDate and checkOut is after checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is after checkInDate and checkOut is before checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().plusDays(1), BOOKING.getCheckOutDate().minusDays(1)));
        // the cas if checkIn is equal checkInDate and checkOut is equal checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate()));
        // the cas if checkIn is equal checkInDate and checkOut is after checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is equal checkInDate and checkOut is before checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate().minusDays(1)));
        // the cas if checkIn is before checkInDate and checkOut is equal checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckOutDate()));
        // the cas if checkIn is after checkInDate and checkOut is equal checkOutDate
        assertFalse(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().plusDays(1), BOOKING.getCheckOutDate()));
    }

    @Test
    public void should_return_true_when_room_is_available() {
        // the cas if checkIn is before checkInDate and checkOut is before checkInDate
        assertTrue(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(2), BOOKING.getCheckInDate().minusDays(1)));
        // the cas if checkIn is after checkOutDate and checkOut is after checkOutDate
        assertTrue(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate().plusDays(1), BOOKING.getCheckOutDate().plusDays(2)));
        // the cas if checkIn is before checkInDate and checkOut is equal checkInDate
        assertTrue(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckInDate()));
        // the cas if checkIn is equal checkOutDate and checkOut is after checkOutDate
        assertTrue(MANAGE_HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate(), BOOKING.getCheckOutDate().plusDays(1)));
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
    public void should_return_booking_when_search() throws BookingNotFoundException, ParamNotValidException, RoomNotFoundException {
        // the cas if booking with date equals checkInDate
        Booking result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckInDate());
        assertEquals(BOOKING, result);
        // the cas if booking with date equals checkOutDate
        result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckOutDate());
        assertEquals(BOOKING, result);
        // the cas if booking with date between checkInDate and checkOutDate
        result = MANAGE_HOTEL.searchBooking(BOOKING.getRoomNumber(), BOOKING.getFullName(), BOOKING.getCheckInDate().plusDays(1));
        assertEquals(BOOKING, result);
    }
}