package be.kdg.sa.backend.api;

import be.kdg.sa.backend.domain.restaurant.Restaurant;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantDto(UUID id,
                         String name,
                         List<DishDto> dishes
) {
    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(restaurant.id(),
                restaurant.name(),
                restaurant.dishes().stream().map(DishDto::from).toList());
    }
    public record DishDto(UUID id,
                          String name,
                          String description,
                          BigDecimal price,
                          int preperationTime
    ) {
        public static DishDto from(Restaurant.Dish dish) {
            return new DishDto(dish.id(),
                    dish.name(),
                    dish.description(),
            dish.price(),
                    dish.preperationTime());
        }
    }
}