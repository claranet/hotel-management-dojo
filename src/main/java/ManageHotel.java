import entities.Booking;
import entities.Hotel;
import entities.Room;
import exceptions.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
public class ManageHotel {

    private Hotel hotel;

    public Booking searchBooking(int roomNumber, String fullName, LocalDate date) throws BookingNotFoundException, ParamNotValidException {
        //check if param is valid
        if (fullName == null || fullName.isEmpty()) {
            throw new ParamNotValidException();
        }

        // TODO : check if room exists

        //get all bookings for this room
        List<Booking> bookingsForRoom = hotel.getBookingsForRoom(roomNumber);

        //search booking
        return bookingsForRoom.stream()
                .filter(booking -> booking.getFullName().equals(fullName))
                .filter(booking -> (date.isAfter(booking.getCheckInDate()) && date.isBefore(booking.getCheckOutDate())) || date.isEqual(booking.getCheckInDate()) || date.isEqual(booking.getCheckOutDate()))
                .findFirst()
                .orElseThrow(BookingNotFoundException::new);
    }

    public void bookRoom(int roomNumber, LocalDate checkInDate, LocalDate checkOutDate, String fullName) throws RoomNotAvailableException, RoomNotFoundException, BookingNotValidException, ParamNotValidException {
        //check if param is valid
        if (checkInDate == null || checkOutDate == null || fullName == null || fullName.isEmpty() || checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(LocalDate.now()) || checkInDate.isEqual(checkOutDate)) {
            throw new ParamNotValidException();
        }

        //check if room exists
        if (!hotel.checkRoomExists(roomNumber)) {
            throw new RoomNotFoundException();
        }

        //check if room is available
        if (!hotel.checkRoomAvailability(roomNumber, checkInDate, checkOutDate)) {
            throw new RoomNotAvailableException();
        }

        //create booking
        Booking booking = Booking.builder()
                .reference(Booking.nextReferenceCount())
                .roomNumber(roomNumber)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .fullName(fullName)
                .build();

        //book room
        hotel.addBooking(booking);
    }

    public void cancelBooking(int reference) {
        // TODO : implement this method
    }

    public List<Room> suggestRoom(int capacity, LocalDate checkInDate, LocalDate checkOutDate) {
        // TODO : implement this method
        return Collections.emptyList();
    }
}
