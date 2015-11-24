package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * Created by tom on 23.11.15.
 */
public class Budget {

    private ObjectProperty<BigDecimal> amount;

    public Budget(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
    }

    public BigDecimal getAmount() {
        return amount.get();
    }

    public ObjectProperty<BigDecimal> getAmountProperty() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
    }
}
