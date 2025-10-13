package be.kdg.sa.backend.domain.restaurant;

import be.kdg.sa.backend.infrastructure.restaurantcatalog.AllRestaurantResponse;

import java.util.UUID;

public record AllRestaurant(UUID id,
                            String name,
                            String restaurantType,
                            boolean isOpen,
                            String priceCategory,
                            String logo) {
    public static AllRestaurant from(AllRestaurantResponse restaurant){
        return new AllRestaurant(
                restaurant.id(),
                restaurant.name(),
                restaurant.restaurantType(),
                restaurant.isOpen(),
                restaurant.priceCategory(),
                restaurant.logo());
    }
}
