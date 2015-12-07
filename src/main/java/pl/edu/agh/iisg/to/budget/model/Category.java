package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by tom on 23.11.15.
 */
public class Category {
    private StringProperty name;
    private ObjectProperty<Category> parent;
    private ObjectProperty<Budget> budget;

    public ObjectProperty<Budget> getBudget() {
        return budget;
    }

    public Category setBudget(Budget budget) {
        this.budget = new SimpleObjectProperty<Budget>(budget);
        return this;
    }

    public StringProperty getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = new SimpleStringProperty(name);
        return this;
    }

    public Category getParent() {
        if (parent != null)
            return parent.get();
        return null;
    }


    public Category setParent(Category parent) {
        this.parent = new SimpleObjectProperty<>(parent);
        return this;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
