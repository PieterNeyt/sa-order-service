package be.kdg.sa.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@ToString
public class Order {
    public OrderOccasion orderOccasion;
    public UUID orderId;
    //public List<Dish> dishes


    public Order(OrderOccasion orderOccasion) {
        this.orderOccasion = orderOccasion;
        // lijst met dishes moet hier ook
    }
}
