package be.kdg.sa.backend.infrastructure.restaurantcatalog;

import be.kdg.sa.backend.domain.restaurant.AllRestaurant;
import be.kdg.sa.backend.domain.restaurant.Restaurant;
import be.kdg.sa.backend.domain.restaurant.RestaurantCatalog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

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
    public Optional<List<AllRestaurant>>getRestaurants() {
        try {
            final List<AllRestaurantResponse> restaurants = restClient
                    .get()
                    .uri("")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<AllRestaurantResponse>>() {});

            return Optional.ofNullable(restaurants.stream().map(AllRestaurant::from).toList());

        } catch (final HttpStatusCodeException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Restaurant> getRestaurantById(UUID restaurantId) {
        try {
            final RestaurantResponse restaurants = restClient
                    .get()
                    .uri("/"+restaurantId)
                    .retrieve()
                    .body(RestaurantResponse.class);

            return Optional.ofNullable(Restaurant.from(restaurants));

        } catch (final HttpStatusCodeException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Restaurant.Dish> getDishById(UUID dishId) {
        try {
            final RestaurantResponse.DishResponse dish = restClient
                    .get()
                    .uri("/dish/"+dishId)
                    .retrieve()
                    .body(RestaurantResponse.DishResponse.class);

            return Optional.ofNullable(Restaurant.Dish.from(dish));

        } catch (final HttpStatusCodeException e) {
            return Optional.empty();
        }
    }


}
