package exceptions;

public class RoomNotAvailableException extends Exception {

    public RoomNotAvailableException() {
        super("Room not available");
    }
}
