package be.kdg.sa.backend.domain.order.orderline;

import lombok.Getter;
import org.jmolecules.ddd.annotation.Entity;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Entity
@Getter
public class OrderLine {
    private final DishId dishId;
    private final BigDecimal price;
    private final String name;
    private int quantity;
    private final int preparationTime;

    public OrderLine(DishId dishId, BigDecimal price, int quantity,String name,  int preparationTime) {
        this.name = name;
        Assert.notNull(dishId, "dishId must not be null");
        this.dishId = dishId;

        validateQuantity(quantity);
        this.quantity = quantity;

        validatePrice(price);
        this.price = price;

        validatePreparationTime(preparationTime);
        this.preparationTime = preparationTime;
    }


    public void validateQuantity(int quantity) {
        Assert.isTrue(quantity > 0, "quantity must be greater than 0");
    }
    public void validatePrice(BigDecimal price) {
        Assert.isTrue(price.compareTo(BigDecimal.ZERO)>0, "price must be greater than 0");
    }
    public void validatePreparationTime(int preparationTime) {
        Assert.isTrue(preparationTime >0, "preparationTime must be greater than 0");
    }

    public boolean isForDishWithPrice(DishId dishId, BigDecimal price) {
        return  dishId.equals(this.dishId) &&  this.price.compareTo(price) == 0;
    }

    public void addQuantity(int quantity) {
        if(quantity <= 0)
            throw new IllegalArgumentException("quantity must be greater than 0");

        this.quantity += quantity;
    }
}
