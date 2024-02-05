package entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HotelTest {

    private Room ROOM;
    private Hotel HOTEL;
    private Booking BOOKING;

    @BeforeEach
    public void setUp() {
        ROOM = Room.builder().roomNumber(1).capacity(2).price(70).build();
        BOOKING = Booking.builder().reference(0).roomNumber(1).fullName("John Doe").checkInDate(LocalDate.now().plusMonths(2)).checkOutDate(LocalDate.now().plusMonths(2).plusDays(3)).build();
        HOTEL = Hotel.builder()
                .rooms(new ArrayList<>(Collections.singletonList(ROOM)))
                .bookings(new ArrayList<>(Collections.singletonList(BOOKING))).build();
    }

    /**
     * Test for checkRoomAvailability method.
     */

    @Test
    public void should_return_false_when_room_is_not_available() {
        // the cas if checkIn is before checkInDate and checkOut is after checkInDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckInDate().plusDays(1)));
        // the cas if checkIn is before checkOutDate and checkOut is after checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate().minusDays(1), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is before checkInDate and checkOut is after checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is after checkInDate and checkOut is before checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().plusDays(1), BOOKING.getCheckOutDate().minusDays(1)));
        // the cas if checkIn is equal checkInDate and checkOut is equal checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate()));
        // the cas if checkIn is equal checkInDate and checkOut is after checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate().plusDays(1)));
        // the cas if checkIn is equal checkInDate and checkOut is before checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate(), BOOKING.getCheckOutDate().minusDays(1)));
        // the cas if checkIn is before checkInDate and checkOut is equal checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckOutDate()));
        // the cas if checkIn is after checkInDate and checkOut is equal checkOutDate
        assertFalse(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().plusDays(1), BOOKING.getCheckOutDate()));
    }

    @Test
    public void should_return_true_when_room_is_available() {
        // the cas if checkIn is before checkInDate and checkOut is before checkInDate
        assertTrue(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(2), BOOKING.getCheckInDate().minusDays(1)));
        // the cas if checkIn is after checkOutDate and checkOut is after checkOutDate
        assertTrue(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate().plusDays(1), BOOKING.getCheckOutDate().plusDays(2)));
        // the cas if checkIn is before checkInDate and checkOut is equal checkInDate
        assertTrue(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckInDate().minusDays(1), BOOKING.getCheckInDate()));
        // the cas if checkIn is equal checkOutDate and checkOut is after checkOutDate
        assertTrue(HOTEL.checkRoomAvailability(ROOM.getRoomNumber(), BOOKING.getCheckOutDate(), BOOKING.getCheckOutDate().plusDays(1)));
    }
}