import entities.Booking;
import entities.Room;
import exceptions.BookingNotFoundException;
import exceptions.ParamNotValidException;
import exceptions.RoomNotAvailableException;
import exceptions.RoomNotFoundException;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ManageHotel {

    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    private boolean checkRoomExists(int roomNumber) {
        return rooms.stream()
                .anyMatch(r -> r.getRoomNumber() == roomNumber);
    }

    private List<Booking> getBookingsForRoom(int roomNumber) {
        return bookings.stream()
                .filter(b -> b.getRoomNumber() == roomNumber)
                .collect(Collectors.toList());
    }

    public boolean checkRoomAvailability(int roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        //get all bookings for this room
        List<Booking> bookingsForRoom;
        bookingsForRoom = getBookingsForRoom(roomNumber);

        //check if room is available
        if(bookingsForRoom.isEmpty()) {
            return true;
        }
        for(Booking booking : bookingsForRoom) {
            if(((checkInDate.isBefore(booking.getCheckInDate()) || checkInDate.isEqual(booking.getCheckInDate())) && checkOutDate.isAfter(booking.getCheckInDate()))
                    || ((checkInDate.isBefore(booking.getCheckOutDate())) && (checkOutDate.isAfter(booking.getCheckOutDate()) || checkOutDate.isEqual(booking.getCheckOutDate())))
                    || ((checkInDate.isAfter(booking.getCheckInDate()) || checkInDate.isEqual(booking.getCheckInDate())) && (checkOutDate.isBefore(booking.getCheckOutDate()) || checkOutDate.isEqual(booking.getCheckOutDate())))) {
                return false;
            }
        }
        return true;
    }

    public Booking searchBooking(int roomNumber, String fullName, LocalDate date) throws BookingNotFoundException, ParamNotValidException {
        //check if param is valid
        if (fullName == null || fullName.isEmpty()) {
            throw new ParamNotValidException();
        }

        // TODO : check if room exists

        //get all bookings for this room
        List<Booking> bookingsForRoom = getBookingsForRoom(roomNumber);

        //search booking
        return bookingsForRoom.stream()
                .filter(booking -> booking.getFullName().equals(fullName))
                .filter(booking -> (date.isAfter(booking.getCheckInDate()) && date.isBefore(booking.getCheckOutDate())) || date.isEqual(booking.getCheckInDate()) || date.isEqual(booking.getCheckOutDate()))
                .findFirst()
                .orElseThrow(BookingNotFoundException::new);
    }

    public void bookRoom(int roomNumber, LocalDate checkInDate, LocalDate checkOutDate, String fullName) throws RoomNotAvailableException, RoomNotFoundException, ParamNotValidException {
        //check if param is valid
        if (checkInDate == null || checkOutDate == null || fullName == null || fullName.isEmpty() || checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(LocalDate.now()) || checkInDate.isEqual(checkOutDate)) {
            throw new ParamNotValidException();
        }

        //check if room exists
        if (!checkRoomExists(roomNumber)) {
            throw new RoomNotFoundException();
        }

        //check if room is available
        if (!checkRoomAvailability(roomNumber, checkInDate, checkOutDate)) {
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
        bookings.add(booking);
    }

    public void cancelBooking(int reference) {
        // TODO : implement this method
    }

    public List<Room> suggestRoom(int capacity, LocalDate checkInDate, LocalDate checkOutDate) {
        // TODO : implement this method
        return Collections.emptyList();
    }
}
