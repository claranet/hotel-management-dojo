package entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class Room {

    private int roomNumber;
    private int capacity;
    private double price;
}
