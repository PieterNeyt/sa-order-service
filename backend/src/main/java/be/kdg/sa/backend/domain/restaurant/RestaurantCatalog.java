package be.kdg.sa.backend.domain.restaurant;

import be.kdg.sa.backend.api.dto.CheckoutResponseDto;
import be.kdg.sa.backend.domain.order.RestaurantId;
import be.kdg.sa.backend.domain.order.orderline.DishId;
import be.kdg.sa.backend.infrastructure.restaurantcatalog.CheckoutDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantCatalog {
    Optional<List<AllRestaurant>> getRestaurants();

    Optional<Restaurant> getRestaurantById(RestaurantId restaurantId);

    Optional<Restaurant.Dish> getDishById(DishId dishId);

    CheckoutResponseDto checkShoppingCart(CheckoutDto shoppingCart);
}
