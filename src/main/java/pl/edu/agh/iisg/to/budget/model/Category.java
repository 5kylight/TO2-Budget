package pl.edu.agh.iisg.to.budget.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 23.11.15.
 */
public class Category {
    private StringProperty name;
    private ObjectProperty<Category> parentCategory;
    private ObjectProperty<Budget> budget;
    private List<ObjectProperty<Category>> subCategories = new ArrayList<>();

    public List<ObjectProperty<Category>> getSubCategories() {
        return subCategories;
    }

    public void addSubCategories(List<ObjectProperty<Category>> subCategories) {
        this.subCategories.addAll(subCategories);
    }


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
        if (parentCategory != null)
            return parentCategory.get();
        return null;
    }


    public Category setParent(Category parent) {
        this.parentCategory = new SimpleObjectProperty<>(parent);
        return this;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
