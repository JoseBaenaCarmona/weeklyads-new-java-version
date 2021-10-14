package domain;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;


@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Habitant {

    Optional<House> house;
    String dni;
    int age;
}
