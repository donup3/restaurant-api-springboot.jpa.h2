package com.dong.restaurant.service;

import com.dong.restaurant.domain.MenuItem;
import com.dong.restaurant.domain.Restaurant;
import com.dong.restaurant.exception.RestaurantNotFoundException;
import com.dong.restaurant.repsoitory.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class RestaurantServiceTest {
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        restaurantService = new RestaurantService(restaurantRepository);

        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(11L)
                .name("밥집")
                .address("서울")
                .build());

        List<MenuItem> menuItems = Arrays.asList(MenuItem.builder().name("떡볶이").build());
        Restaurant restaurant = Restaurant.builder().id(1L).name("분식집").address("서울").menuItems(menuItems).build();
        restaurant.addMenuItem(menuItems);

        given(restaurantRepository.findAll()).willReturn(restaurants);
        given(restaurantRepository.findById(1L)).willReturn(of(restaurant));
    }

    @Test
    public void getRestaurant() {
        Restaurant restaurant = restaurantService.getRestaurant(1L);

        assertEquals(restaurant.getId(), 1L);
    }

    @Test
    public void getRestaurantNotExisted() {
        assertThrows(RestaurantNotFoundException.class,
                () -> restaurantService.getRestaurant(222L));
    }


    @Test
    public void getRestaurants() {
        List<Restaurant> restaurants = restaurantService.getRestaurants();
        Restaurant restaurant = restaurants.get(0);
        assertEquals(restaurant.getId(), 11L);
    }

    @Test
    public void addRestaurant() {
        Restaurant restaurant = Restaurant.builder().name("분식집").address("서울").build();
        Restaurant saveRestaurant = Restaurant.builder().id(123L).name("분식집").address("서울").build();

        given(restaurantRepository.save(any())).willReturn(saveRestaurant);

        Restaurant newRestaurant = restaurantService.addRestaurant(restaurant);

        assertEquals(newRestaurant.getId(), 123L);
    }

    @Test
    public void updateRestaurant() {
        Restaurant restaurant = Restaurant.builder().id(123L).name("밥집").address("서울").build();
        given(restaurantRepository.findById(123L)).willReturn(Optional.of(restaurant));

        restaurantService.updateRestaurant(123L, "고기집", "강릉");

        assertEquals(restaurant.getName(), "고기집");
        assertEquals(restaurant.getAddress(), "강릉");
    }
}