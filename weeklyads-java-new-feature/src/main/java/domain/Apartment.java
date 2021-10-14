package domain;


import lombok.Data;

import java.util.Optional;

@Data
public class Apartment extends House {

    private int numberFloors;

    public Apartment(Address address, Optional<Parking> parking, int numberFloors) {
        super(address, parking);
        this.numberFloors = numberFloors;
    }
}
