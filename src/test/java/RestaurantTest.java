import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RestaurantTest {
    Restaurant restaurant;

    @BeforeEach
    public void beforeEachTest() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time(){
        //Arrange
        LocalTime currentTime = LocalTime.parse("12:00:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.doReturn(currentTime).when(spyRestaurant).getCurrentTime();

        //Act
        Boolean isOpen = spyRestaurant.isRestaurantOpen();

        //Assert
        assertEquals(true, isOpen);
        Mockito.verify(spyRestaurant).getCurrentTime();
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time(){
        //Arrange
        LocalTime currentTime = LocalTime.parse("23:30:00");
        Restaurant spyRestaurant = Mockito.spy(restaurant);
        Mockito.doReturn(currentTime).when(spyRestaurant).getCurrentTime();

        //Act
        Boolean isOpen = spyRestaurant.isRestaurantOpen();

        //Assert
        assertEquals(false, isOpen);
        Mockito.verify(spyRestaurant).getCurrentTime();
    }
    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>Calculate Order Total<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void returns_accurate_cost_after_adding_one_or_more_items() {
        //Arrange
        String item1Name = "Sweet corn soup";
        int item1Price = 119;
        String item2Name = "Vegetable lasagne";
        int item2Price = 269;
        restaurant.addToMenu(item1Name,item1Price);
        restaurant.addToMenu(item2Name, item2Price);

        ArrayList<String> itemsAdded = new ArrayList<>();
        itemsAdded.add(item1Name);
        itemsAdded.add(item2Name);

        //Act
        Integer expectedCost = item1Price+item2Price;
        Integer actualCost = restaurant.calculateOrderTotal(itemsAdded);

        //Assert
        assertEquals(expectedCost, actualCost);

    }

    @Test
    public void returns_total_cost_as_zero_when_there_are_zero_items_in_the_cart() {
        //Arrange
        ArrayList<String> itemsAdded = new ArrayList<>();

        //Act
        Integer actualCost = restaurant.calculateOrderTotal(itemsAdded);

        //Assert
        assertEquals(0, actualCost);
    }
    //<<<<<<<<<<<<<<<<<<<<Calculate Order Total>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}