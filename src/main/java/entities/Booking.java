package entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class Booking {

    private static int referenceCount = 0;

    private int reference;
    private int roomNumber;
    private String fullName;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public static int nextReferenceCount(){
        return referenceCount++;
    }
}
