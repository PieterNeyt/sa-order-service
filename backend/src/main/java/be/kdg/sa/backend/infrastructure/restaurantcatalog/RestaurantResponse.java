package be.kdg.sa.backend.infrastructure.restaurantcatalog;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record RestaurantResponse(UUID id,
                                 String name,
                                 List<DishResponse> dishes,
                                 String restaurantType,
                                 boolean isOpen,
                                 String priceCategory,
                                 String logo
) {
    public record DishResponse(UUID id,
                          UUID RestaurantId,
                          String name,
                          String description,
                          BigDecimal price,
                          int preparationTime
    ) {
    }
}
