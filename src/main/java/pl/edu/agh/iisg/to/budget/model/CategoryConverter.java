package pl.edu.agh.iisg.to.budget.model;

import javafx.util.StringConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 07.12.15.
 */
public class CategoryConverter extends StringConverter {

    private Map<String, Category> categoryMap = new HashMap<>();

    @Override
    public String toString(Object object) {
        Category category = (Category) object;
        categoryMap.put(category.getName().get(), category);
        return category.getName().get();
    }

    @Override
    public Category fromString(String categoryName) {
        return categoryMap.get(categoryName);
    }
}
