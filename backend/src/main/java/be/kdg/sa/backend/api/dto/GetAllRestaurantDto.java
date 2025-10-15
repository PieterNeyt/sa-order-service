package be.kdg.sa.backend.api.dto;

import be.kdg.sa.backend.domain.restaurant.AllRestaurant;


import java.util.UUID;

public record GetAllRestaurantDto(UUID id,
                                  String name,
                                  String restaurantType,
                                  boolean isOpen,
                                  String  priceCategory,
                                  String logo) {
    public static GetAllRestaurantDto from(AllRestaurant restaurant){
        return new GetAllRestaurantDto(
                restaurant.id(),
                restaurant.name(),
                restaurant.restaurantType(),
                restaurant.isOpen(),
                restaurant.priceCategory(),
                restaurant.logo());
    }
}
