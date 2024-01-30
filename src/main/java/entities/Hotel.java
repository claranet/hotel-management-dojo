package entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Hotel {

    @Builder.Default
    private List<Room> rooms = new ArrayList<>();
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    public boolean checkRoomExists(int roomNumber) {
        return rooms.stream()
                .anyMatch(r -> r.getRoomNumber() == roomNumber);
    }

    public List<Booking> getBookingsForRoom(int roomNumber) {
        return bookings.stream()
                .filter(b -> b.getRoomNumber() == roomNumber)
                .toList();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
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
}
