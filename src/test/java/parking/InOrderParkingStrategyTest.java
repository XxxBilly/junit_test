package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InOrderParkingStrategyTest {

    @Test
    public void testCreateReceipt_givenACarAndAParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createReceipt()
         * With using Mockito to mock the input parameter */
        Car car = new Car("car");
        ParkingLot parkingLot = new ParkingLot("Parking Lot 1", 1);

        Car mockCar = mock(Car.class);
        when(mockCar.getName()).thenReturn("car2");

        ParkingLot mockParkingLot = mock(ParkingLot.class);
        when(mockParkingLot.getName()).thenReturn("Parking Lot 2");

        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        Receipt receipt = inOrderParkingStrategy.createReceipt(mockParkingLot, mockCar);

        verify(mockCar, times(1)).getName();
        verify(mockParkingLot, times(1)).getName();

        assertEquals("car2", receipt.getCarName());
        assertEquals("Parking Lot 2", receipt.getParkingLotName());
    }

    @Test
    public void testCreateNoSpaceReceipt_givenACar_thenGiveANoSpaceReceipt() {

        /* Exercise 1, Write a test case on InOrderParkingStrategy.createNoSpaceReceipt()
         * With using Mockito to mock the input parameter */

        Car car = new Car("car");
        ParkingLot parkingLot = new ParkingLot("Parking Lot 1", 1);

        Car mockCar = mock(Car.class);
        when(mockCar.getName()).thenReturn("car2");


        InOrderParkingStrategy inOrderParkingStrategy = new InOrderParkingStrategy();
        Receipt receipt = inOrderParkingStrategy.createNoSpaceReceipt(mockCar);

        verify(mockCar, times(1)).getName();


        assertEquals("car2", receipt.getCarName());
        assertEquals("No Parking Lot", receipt.getParkingLotName());

    }

    @Test
    public void testPark_givenNoAvailableParkingLot_thenCreateNoSpaceReceipt() {

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for no available parking lot */

        Car leoCar = new Car("LeoCar");


        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());

        Receipt receipt = inOrderParkingStrategy.park(null, leoCar);

        verify(inOrderParkingStrategy).createNoSpaceReceipt(leoCar);
    }

    @Test
    public void testPark_givenThereIsOneParkingLotWithSpace_thenCreateReceipt() {

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot */

        Car car = new Car("leoCar");
        ParkingLot parkingLot = new ParkingLot("parking lot", 1);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);

        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());

        Receipt receipt = inOrderParkingStrategy.park(parkingLots, car);

        verify(inOrderParkingStrategy).createReceipt(parkingLot, car);

        assertEquals("leoCar", receipt.getCarName());
        assertEquals("parking lot", receipt.getParkingLotName());
    }

    @Test
    public void testPark_givenThereIsOneFullParkingLot_thenCreateReceipt() {

        /* Exercise 2: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for one available parking lot but it is full */

        Car car = new Car("leoCar");
        ParkingLot parkingLot = new ParkingLot("parking lot", 1);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);

        parkingLot.getParkedCars().add(new Car("newCar"));

        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());
        inOrderParkingStrategy.park(parkingLots,car);

        verify(inOrderParkingStrategy,times(0)).createReceipt(parkingLot,car);
        verify(inOrderParkingStrategy).createNoSpaceReceipt(car);

    }

    @Test
    public void testPark_givenThereIsMultipleParkingLotAndFirstOneIsFull_thenCreateReceiptWithUnfullParkingLot() {

        /* Exercise 3: Test park() method. Use Mockito.spy and Mockito.verify to test the situation for multiple parking lot situation */

        Car car = new Car("leoCar");
        ParkingLot parkingLot1 = new ParkingLot("parking lot1", 1);
        parkingLot1.getParkedCars().add(new Car("newCar"));
        ParkingLot parkingLot2 = new ParkingLot("parking lot2", 2);

        List<ParkingLot> parkingLots = Arrays.asList(parkingLot1,parkingLot2);

        InOrderParkingStrategy inOrderParkingStrategy = spy(new InOrderParkingStrategy());

        Receipt receipt = inOrderParkingStrategy.park(parkingLots, car);

        verify(inOrderParkingStrategy).createReceipt(parkingLot2,car);

        assertEquals("leoCar", receipt.getCarName());
        assertEquals("parking lot2", receipt.getParkingLotName());


    }


}
