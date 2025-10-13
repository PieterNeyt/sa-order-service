package be.kdg.sa.backend.domain.restaurant;

import be.kdg.sa.backend.infrastructure.restaurantcatalog.RestaurantResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record Restaurant(UUID id,
                                 String name,
                                 List<Dish> dishes
) {
    public static Restaurant from(RestaurantResponse restaurantResponse) {
        return new Restaurant(restaurantResponse.id(),
                restaurantResponse.name(),
                restaurantResponse.dishes().stream().map(Dish::from).toList());
    }
    public record Dish(UUID id,
                               UUID RestaurantId,
                               String name,
                               String description,
                               BigDecimal price,
                               int preperationTime
    ) {
        public static Dish from(RestaurantResponse.DishResponse dishResponse) {
            return new Dish(dishResponse.id(),
                    dishResponse.RestaurantId(),
                    dishResponse.name(),
                    dishResponse.description(),
                    dishResponse.price(),
                    dishResponse.preparationTime());
        }
    }
}