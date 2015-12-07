package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;

/**
 * Created by tom on 23.11.15.
 */
public class Budget {

    private ObjectProperty<Category> category;

    private ObjectProperty<BigDecimal> amount;
    private ObjectProperty<BigDecimal> spent;
    private ObjectProperty<BigDecimal> balance;




    public Budget(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
    }

    public Budget() {
    }

    public ObjectProperty<BigDecimal> getAmount() {
        return amount;
    }

    public Budget setAmount(BigDecimal amount) {
        this.amount = new SimpleObjectProperty<>(amount);
        return this;
    }

    public ObjectProperty<BigDecimal> amountProperty() {
        return amount;
    }

    public ObjectProperty<BigDecimal> getSpent() {
        return spent;
    }

    public Budget setSpent(BigDecimal spent) {
        this.spent = new SimpleObjectProperty<>(spent);
        return this;
    }

    public ObjectProperty<Category> getCategory() {
        return category;

    }

    public Budget setCategory(Category category) {
        this.category = new SimpleObjectProperty<>(category);
        return this;
    }

    public ObjectProperty<BigDecimal> getBalance() {
        return balance;
    }

    public Budget setBalance(BigDecimal balance) {
        this.balance = new SimpleObjectProperty<>(balance);
        return this;
    }
}
