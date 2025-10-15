package be.kdg.sa.backend.domain.restaurant;

import be.kdg.sa.backend.api.dto.CheckoutResponseDto;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantCatalog {
    Optional<List<AllRestaurant>> getRestaurants();

    Optional<Restaurant> getRestaurantById(UUID restaurantId);

    Optional<Restaurant.Dish> getDishById(UUID dishId);

    CheckoutResponseDto checkShoppingCart(CheckoutDto shoppingCart);
}
