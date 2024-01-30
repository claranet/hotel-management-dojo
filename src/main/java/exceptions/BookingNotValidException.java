package exceptions;

public class BookingNotValidException extends Exception {

    public BookingNotValidException() {
        super("Booking not valid");
    }
}
