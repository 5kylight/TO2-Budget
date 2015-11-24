package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * Created by tom on 23.11.15.
 */
public class Transaction {
    private ObjectProperty<Category> category;
    private ObjectProperty<BigDecimal> value;
    private StringProperty name;
    private BooleanProperty isDone;
    private ObjectProperty<LocalTime> localTime;


    public Transaction() {
    }

    public BooleanProperty getIsDone() {
        return isDone;
    }

    public Transaction setIsDone(Boolean isDone) {
        this.isDone = new SimpleBooleanProperty(isDone);
        return this;

    }

    public ObjectProperty<LocalTime> getLocalTime() {
        return localTime;
    }

    public Transaction setLocalTime(LocalTime localTime) {
        this.localTime = new SimpleObjectProperty<LocalTime>(localTime);
        return this;
    }

    public ObjectProperty<Category> getCategory() {
        return category;
    }

    public Transaction setCategory(Category category) {
        this.category = new SimpleObjectProperty<Category>(category);
        return this;
    }

    public ObjectProperty<BigDecimal> getValue() {
        return value;
    }

    public Transaction setValue(BigDecimal value) {
        this.value = new SimpleObjectProperty<BigDecimal>(value);
        return this;
    }

    public StringProperty getName() {
        return name;
    }

    public Transaction setName(String name) {
        this.name = new SimpleStringProperty(name);
        return this;
    }


}
