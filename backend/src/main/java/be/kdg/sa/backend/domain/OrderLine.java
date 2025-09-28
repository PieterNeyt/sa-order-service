package be.kdg.sa.backend.domain;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Entity
@Getter
public class OrderLine {
    private final DishId dishId;
    private final BigDecimal price;
    private int quantity;

    public OrderLine(DishId dishId, BigDecimal price, int quantity) {
        Assert.notNull(dishId, "dishId must not be null");
        this.dishId = dishId;

        validateQuantity(quantity);
        this.quantity = quantity;

        validatePrice(price);
        this.price = price;
    }


    public void validateQuantity(int quantity) {
        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
    }
    public void validatePrice(BigDecimal price) {
        Assert.isTrue(price.compareTo(BigDecimal.ZERO)<=0, "price must be greater than 0");
    }

    public boolean isForDishWithPrice(DishId dishId, BigDecimal price) {
        return  dishId.equals(this.dishId) &&  price.equals(this.price);
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }
}
