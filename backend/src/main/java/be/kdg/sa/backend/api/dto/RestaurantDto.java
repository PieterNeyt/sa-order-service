package be.kdg.sa.backend.api.dto;

import be.kdg.sa.backend.domain.restaurant.Restaurant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id,
                         String name,
                         List<DishDto> dishes,
                            String restaurantType,
                            boolean isOpen,
                            String  priceCategory,
                            String logo
) {
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(restaurant.id(),
                restaurant.name(),
                restaurant.dishes().stream().map(DishDto::from).toList(),
                restaurant.restaurantType(),
                restaurant.isOpen(),
                restaurant.priceCategory(),
                restaurant.logo());
    }
    public record DishDto(UUID id,
                          UUID restaurantId,
                          String name,
                          String description,
                          BigDecimal price,
                          int preparationTime
    ) {
        public static DishDto from(Restaurant.Dish dish) {
            return new DishDto(dish.id(),
                    dish.RestaurantId(),
                    dish.name(),
                    dish.description(),
                    dish.price(),
                    dish.preperationTime());
        }
    }
}