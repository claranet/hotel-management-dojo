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

/**
 * This class is used to manage the hotel
 */
@Getter
@Builder
public class ManageHotel {

    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    /**
     * This method is used to check if a room exists
     * @param roomNumber the room number
     * @return true if the room exists, false otherwise
     */
    private boolean checkRoomExists(int roomNumber) {
        return rooms.stream()
                .anyMatch(r -> r.getRoomNumber() == roomNumber);
    }

    /**
     * This method is used to get all bookings for a room
     * @param roomNumber the room number
     * @return a list of bookings for the room
     */
    private List<Booking> getBookingsForRoom(int roomNumber) {
        return bookings.stream()
                .filter(b -> b.getRoomNumber() == roomNumber)
                .collect(Collectors.toList());
    }

    /**
     * This method is used to check if a room is available
     * @param roomNumber the room number
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return true if the room is available, false otherwise
     */
    public boolean checkRoomAvailability(int roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> bookingsForRoom = getBookingsForRoom(roomNumber);

        return bookingsForRoom.stream()
                .noneMatch(booking -> checkInDate.isBefore(booking.getCheckOutDate()) && checkOutDate.isAfter(booking.getCheckInDate()));
    }

    /**
     * This method is used to search for a booking
     * @param roomNumber the room number
     * @param fullName the full name
     * @param date the date
     * @return the booking
     * @throws BookingNotFoundException if the booking is not found
     * @throws ParamNotValidException if the parameters are not valid
     * @throws RoomNotFoundException 
     */
    public Booking searchBooking(int roomNumber, String fullName, LocalDate date) throws BookingNotFoundException, ParamNotValidException, RoomNotFoundException {
        //check if param is valid
        if (fullName == null || fullName.isEmpty()  || date == null) {
            throw new ParamNotValidException();
        }

        // check if room exists
        if (!checkRoomExists(roomNumber)) {
            throw new RoomNotFoundException();
        }

        //get all bookings for this room
        List<Booking> bookingsForRoom = getBookingsForRoom(roomNumber);

        //search booking
        return bookingsForRoom.stream()
                .filter(booking -> booking.getFullName().equals(fullName))
                .filter(booking -> (date.isAfter(booking.getCheckInDate()) && date.isBefore(booking.getCheckOutDate())) || date.isEqual(booking.getCheckInDate()) || date.isEqual(booking.getCheckOutDate()))
                .findFirst()
                .orElseThrow(BookingNotFoundException::new);
    }

    /**
     * This method is used to book a room
     * @param roomNumber the room number
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @param fullName the full name
     * @throws RoomNotAvailableException if the room is not available
     * @throws RoomNotFoundException if the room is not found
     * @throws ParamNotValidException if the parameters are not valid
     */
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

    /**
     * This method is used to cancel a booking
     * @param reference the reference
     */
    public void cancelBooking(int reference) {
        // TODO : implement this method
    }

    /**
     * This method is used to suggest a room
     * @param capacity the capacity
     * @param checkInDate the check-in date
     * @param checkOutDate the check-out date
     * @return a list of rooms
     */
    public List<Room> suggestRoom(int capacity, LocalDate checkInDate, LocalDate checkOutDate) {
        // TODO : implement this method
        return Collections.emptyList();
    }
}
