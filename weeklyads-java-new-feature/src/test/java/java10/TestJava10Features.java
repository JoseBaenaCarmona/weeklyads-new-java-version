package java10;


import domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.*;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class TestJava10Features {
    Address address2 = new Address("Benjamin Franklin ", 44);
    Parking parking2 = new Parking(4);

    @Test
    public void testTypeInferenceCollections(){
        var home = new Apartment(address2,Optional.of(parking2),4);
        assertTrue(home instanceof Apartment);
        assertTrue(home instanceof House );

    }
}
