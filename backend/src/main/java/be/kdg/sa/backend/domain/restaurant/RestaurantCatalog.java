package be.kdg.sa.backend.domain.restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantCatalog {
    Optional<List<Restaurant>> getRestaurants();
}
