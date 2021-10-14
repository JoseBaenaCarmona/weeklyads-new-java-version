package domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

@Data
@AllArgsConstructor
public class House {

    Address address;
    Optional<Parking> parking;
}
