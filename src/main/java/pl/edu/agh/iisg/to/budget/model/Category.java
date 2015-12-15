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
    private List<ObjectProperty<Category>> subCategories = new ArrayList<>();

    public List<ObjectProperty<Category>> getSubCategories() {
        return subCategories;
    }

    public void addSubCategories(List<ObjectProperty<Category>> subCategories) {
        this.subCategories.addAll(subCategories);
    }

    public void addSubCategories(Category ... categories) {
        for(Category category : categories) {
            this.subCategories.add(new SimpleObjectProperty<>(category));
        }
    }

    public void removeSubcategory(Category category) {
        subCategories.remove(category);
    }


    public StringProperty getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = new SimpleStringProperty(name);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!name.equals(category.name)) return false;
        if (parentCategory != null ? !parentCategory.equals(category.parentCategory) : category.parentCategory != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parentCategory != null ? parentCategory.hashCode() : 0);
        return result;
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

    public boolean isParent(){
        if(this.getParent()==null)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return name.get();
    }
}
