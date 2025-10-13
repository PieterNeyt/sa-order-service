package be.kdg.sa.backend.domain.restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantCatalog {
    Optional<List<AllRestaurant>> getRestaurants();

    Optional<Restaurant> getRestaurantById(UUID restaurantId);
}
