package pl.edu.agh.iisg.to.budget.model;

import java.math.BigDecimal;

/**
 * Created by tom on 23.11.15.
 */
public class Transaction {
    private Category category;
    private BigDecimal value;
    private String name;
    private Boolean isDone;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
