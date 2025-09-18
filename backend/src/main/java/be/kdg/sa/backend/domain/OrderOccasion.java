package be.kdg.sa.backend.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderOccasion {
    public Client cLient;
    public Order order;

    public OrderOccasion(Client cLient, Order order) {
        this.cLient = cLient;
        this.order = order;
    }
}
