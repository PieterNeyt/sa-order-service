package be.kdg.sa.backend.infrastructure.restaurantcatalog;

import java.util.UUID;

public record AllRestaurantResponse(UUID id,
                                    String name,
                                    String restaurantType,
                                    boolean isOpen,
                                    String priceCategory,
                                    String logo) {
}


