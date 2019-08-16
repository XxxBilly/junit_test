package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class VipParkingStrategyTest {

	@Test
    public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 4, Write a test case on VipParkingStrategy.park()
	    * With using Mockito spy, verify and doReturn */

	    Car car = new Car("LeoCarA");

        ParkingLot parkingLot1 = new ParkingLot("parking lot1", 1);
        parkingLot1.getParkedCars().add(new Car("newCar"));
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot1);

	    VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

	    doReturn(true).when(vipParkingStrategy).isAllowOverPark(car);

	    Receipt receipt = vipParkingStrategy.park(parkingLots,car);

        assertEquals("LeoCarA", receipt.getCarName());
        assertEquals("parking lot1", receipt.getParkingLotName());
    }

    @Test
    public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

        /* Exercise 4, Write a test case on VipParkingStrategy.park()
         * With using Mockito spy, verify and doReturn */
        Car car = new Car("LeoCar");

        ParkingLot parkingLot1 = new ParkingLot("parking lot1", 1);
        parkingLot1.getParkedCars().add(new Car("newCar"));
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot1);

        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

        doReturn(false).when(vipParkingStrategy).isAllowOverPark(car);

        Receipt receipt = vipParkingStrategy.park(parkingLots,car);

        verify(vipParkingStrategy,times(0)).createReceipt(parkingLot1,car);

        assertEquals("LeoCar", receipt.getCarName());
        assertEquals("No Parking Lot", receipt.getParkingLotName());

    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car car = new Car("LeoCarA");

        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCarA")).thenReturn(true);
        doReturn(mCarDao).when(vipParkingStrategy).getCarDao();

        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(car);

        assertTrue(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car car = new Car("LeoCar");

        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCar")).thenReturn(true);
        doReturn(mCarDao).when(vipParkingStrategy).getCarDao();

        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(car);

        assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse(){
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car car = new Car("LeoCarA");

        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCarA")).thenReturn(false);
        doReturn(mCarDao).when(vipParkingStrategy).getCarDao();

        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(car);

        assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */

        Car car = new Car("LeoCar");

        VipParkingStrategy vipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCar")).thenReturn(false);
        doReturn(mCarDao).when(vipParkingStrategy).getCarDao();

        boolean allowOverPark = vipParkingStrategy.isAllowOverPark(car);

        assertFalse(allowOverPark);
    }

    private Car createMockCar(String carName) {
        Car car = mock(Car.class);
        when(car.getName()).thenReturn(carName);
        return car;
    }
}
