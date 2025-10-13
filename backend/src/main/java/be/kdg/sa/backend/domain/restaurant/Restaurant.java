package be.kdg.sa.backend.domain.restaurant;

import be.kdg.sa.backend.api.GetAllRestaurantDto;
import be.kdg.sa.backend.infrastructure.ExternalRestaurantCatalog;

import java.util.UUID;

public record Restaurant(UUID id,
                         String name,
                         String restaurantType,
                         boolean isOpen,
                         String priceCategory,
                         String logo) {
    public static Restaurant from(ExternalRestaurantCatalog.RestaurantResponse restaurant){
        return new Restaurant(
                restaurant.id(),
                restaurant.name(),
                restaurant.restaurantType(),
                restaurant.isOpen(),
                restaurant.priceCategory(),
                restaurant.logo());
    }
}
