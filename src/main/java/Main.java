import entities.Booking;
import entities.Hotel;
import entities.Room;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

public class Main {

    private static final Scanner scanner = new Scanner(in);

    private static final ManageHotel manageHotel = new ManageHotel(Hotel.builder()
            .rooms(Arrays.asList(
                    Room.builder().roomNumber(1).capacity(2).price(70).build(),
                    Room.builder().roomNumber(2).capacity(2).price(80).build(),
                    Room.builder().roomNumber(3).capacity(4).price(130).build(),
                    Room.builder().roomNumber(4).capacity(4).price(130).build(),
                    Room.builder().roomNumber(5).capacity(6).price(180).build(),
                    Room.builder().roomNumber(6).capacity(6).price(190).build()))
            .bookings(new ArrayList<>())
            .build());

    public static void main(String[] args) {
        out.println("Welcome to the domains.Hotel domains.Booking System");
        out.println("------");
        out.println("Rooms:");
        for (Room room : manageHotel.getHotel().getRooms()) {
            out.println(room.toString());
        }

        boolean exit = false;
        while (!exit) {
            out.println("===================================");
            out.println("1. Book a room");
            out.println("2. Cancel a booking");
            out.println("3. Search bookings");
            out.println("4. View all bookings");
            out.println("5. Exit");
            out.println("------");
            out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            out.println("===================================");

            switch (choice) {
                case 1:
                    try {
                        manageHotel.bookRoom(getRoomNumber(), getCheckIngDate(), getCheckOutDate(), getFullName());
                        out.println("Booked successfully");
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        manageHotel.cancelBooking(getReference());
                        out.println("Booking cancelled successfully");
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        Booking booking = manageHotel.searchBooking(getRoomNumber(), getFullName(), getDate());
                        out.println(booking.toString());
                    } catch (Exception e) {
                        out.println(e.getMessage());
                    }
                    break;
                case 4:
                        manageHotel.getHotel().getBookings().forEach(booking -> {
                            out.println(booking.toString());
                    });
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    out.println("Invalid choice");
            }
        }
    }

    private static String getFullName() {
        out.print("Enter full name: ");
        return scanner.next();
    }

    private static LocalDate getCheckIngDate() {
        out.print("Enter check in date (YYYY-MM-DD): ");
        return LocalDate.parse(scanner.next());
    }

    private static LocalDate getCheckOutDate() {
        out.print("Enter check out date (YYYY-MM-DD): ");
        return LocalDate.parse(scanner.next());
    }

    private static int getReference() {
        out.print("Enter reference: ");
        return scanner.nextInt();
    }

    private static LocalDate getDate() {
        out.print("Enter date (YYYY-MM-DD): ");
        return LocalDate.parse(scanner.next());
    }

    private static int getRoomNumber() {
        out.print("Enter room number: ");
        return scanner.nextInt();
    }
}
