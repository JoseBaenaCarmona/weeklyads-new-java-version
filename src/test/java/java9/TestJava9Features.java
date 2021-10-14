package java9;

import domain.Address;
import domain.Habitant;
import domain.House;
import domain.Parking;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class TestJava9Features {
    private List<Habitant> city;
    Address address1 = new Address("Abraham Lincoln", 23);
    Address address2 = new Address("Benjamin Franklin ", 44);
    Address address3 = new Address("Main Avenue ", 22);
    Parking parking1 = new Parking(1);
    Parking parking2 = new Parking(4);
    Parking parking3 = new Parking(null);
    House home1 = new House(address1,Optional.of(parking1));
    House home2 = new House(address2,Optional.of(parking2));
    House home3 = new House(address3,Optional.of(parking3));
    Habitant habitant1 = new Habitant(Optional.of(home1),"dni1",22);
    Habitant habitant2 = new Habitant(Optional.of(home2), "dni2",90);
    Habitant habitant3 = new Habitant(Optional.of(home3), "dni3",40);

    @Before
    public void init() {
        city = List.of(habitant1, habitant2,habitant3);
    }

    @Test
    public void collectionsFactory(){
        List<Habitant> habitantList = List.of(habitant1,habitant2);
        assertEquals(2, habitantList.size());

        Set<Habitant> habitantSet = Set.of(habitant1,habitant2);
        assertEquals(2, habitantSet.size());

        Map<String,Habitant> habitantMap = Map.of(habitant1.getDni(),habitant1,  habitant2.getDni(),habitant2);
        assertEquals(habitantMap.get(habitant1.getDni()), habitant1);
        assertEquals(habitantMap.get(habitant2.getDni()), habitant2);
        assertEquals(2, habitantMap.size());
    }

    @Test
    public void streamOfNullable(){
        Stream<String> documents = city.stream().flatMap(h -> Stream.ofNullable( h.getDni()));
        assertEquals(2,documents.count());
    }

    @Test
    public void streamOfOptional(){
        Optional<Habitant> optHabitant = Optional.of(habitant1);
        Stream<Habitant> streamHabitant = optHabitant.stream();

        assertEquals(1, streamHabitant.count());
    }

    @Test
    public void streamOfOptionalsPipe(){
        List<Integer> slots = city.stream()
                .map(Habitant::getHouse)
                .map(optHouse -> optHouse.flatMap(House::getParking))
                .map(optParking -> optParking.map(Parking::getSlots))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());

        assertEquals(2, slots.size());
    }

    @Test
    public void optionalOrMethod(){
        Habitant habitant = null;
        Optional<Habitant> optHabitant = Optional.ofNullable(habitant).or(() -> Optional.of(habitant1));

        assertEquals(optHabitant.get(),habitant1);
    }

    @Test
    public void optionalIfPresentOrElse(){
        List<String> dnis = new ArrayList<>();
        final String defaultDni = "999999999";
        Optional<Habitant> habitantEmpty = Optional.empty();
        habitantEmpty.ifPresentOrElse(h -> dnis.add(habitant1.getDni()), () -> dnis.add(defaultDni));

        assertEquals(dnis.get(0),defaultDni);
    }

    @Test
    public void streamTakenWhile(){

        List<Habitant> habitants  = Stream.of(habitant1, habitant2,habitant3).sorted(Comparator.comparing(Habitant::getAge)).collect(Collectors.toList());
        List<Habitant> habitantYoungerThant50 =  habitants.stream().takeWhile(h -> h.getAge() < 50).collect(Collectors.toList());

        assertEquals(2,habitantYoungerThant50.size());
    }

    @Test
    public void streamDropWhile(){

        List<Habitant> habitants  = Stream.of(habitant1, habitant2,habitant3).sorted(Comparator.comparing(Habitant::getAge)).collect(Collectors.toList());
        List<Habitant> habitantYoungerThant50 =  habitants.stream().dropWhile(h -> h.getAge() < 50).collect(Collectors.toList());

        assertEquals(1,habitantYoungerThant50.size());
    }

    @Test
    public void remoteAsynchronousApiCallBeforeTimeOut() throws ExecutionException, InterruptedException {
        CompletableFuture<Habitant> habitantFuture  = CompletableFuture
                .supplyAsync(() -> simulateRemoteAPICall(1000)).orTimeout(2000L, TimeUnit.MILLISECONDS);

        Habitant habitantRet = habitantFuture.get();
        assertEquals(habitantRet ,new Habitant(Optional.of(home3), "dni3",40));
    }

    @Test(expected = ExecutionException.class)
    public void remoteAsynchronousApiCallAfterTimeOut() throws ExecutionException, InterruptedException {
        CompletableFuture<Habitant> habitantFuture  = CompletableFuture
                .supplyAsync(() -> simulateRemoteAPICall(2000)).orTimeout(50, TimeUnit.MILLISECONDS);

        Habitant habitantRet = habitantFuture.get();
        assertEquals(habitantRet ,new Habitant(Optional.of(home3), "dni3",40));
    }


    private Habitant simulateRemoteAPICall(long delay){
        try{
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return  new Habitant(Optional.of(home3), "dni3",40);
    }
}
