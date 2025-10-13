package be.kdg.sa.backend.infrastructure;

import be.kdg.sa.backend.domain.Order;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.domain.restaurant.RestaurantCatalog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ExternalRestaurantCatalog implements RestaurantCatalog {
    private final RestClient restClient;

    public ExternalRestaurantCatalog(@Qualifier("restaurantCatalogApi") final RestClient restClient) {
        this.restClient = restClient;
    }


    @Override
    public Optional<List<Restaurant>>getRestaurants() {
        try {
            final List<RestaurantResponse> restaurants = restClient
                    .get()
                    .uri("")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<RestaurantResponse>>() {});

            return Optional.ofNullable(restaurants.stream().map(Restaurant::from).toList());

        } catch (final HttpStatusCodeException e) {
            System.out.println(e.getResponseBodyAsString());
            return Optional.empty();
        }

    }
    public record RestaurantResponse(UUID id,
                             String name,
                             String restaurantType,
                             boolean isOpen,
                             String priceCategory,
                             String logo) {
    }

}
