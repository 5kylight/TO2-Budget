package pl.edu.agh.iisg.to.budget.controller.generators;

import pl.edu.agh.iisg.to.budget.controller.BudgetManager;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.math.BigDecimal;
import java.util.*;

public class DataGenerator {

    BudgetManager budgetManager;

    List<Budget> data;
    Random random = new Random();

    public List<Budget> getData() {
        return data;
    }

    public DataGenerator(BudgetManager budgetManager){
        this.budgetManager = budgetManager;

        List<Category> parentCategories = generateParentCategories();
        budgetManager.setParentCategories(parentCategories);
        List<Category> subcategories = generateSubcategories();
        budgetManager.setSubcategories(subcategories);

        data = budgetManager.getExpenses(generateData());

        //TODO: test data
        for(Budget budget : data){
            budget.setPlanned(budget.getSpent().get().add(new BigDecimal(random.nextInt(10))));
        }
    }

    private List generateSubcategories() {
        List<Category> categories = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Category parentCategory = budgetManager.getParentCategories().get(random.nextInt(budgetManager.getParentCategories().size()));
            Category category = new Category().setName("Category " + i).setParent(parentCategory);
            parentCategory.addSubCategories(category);
            categories.add(category);
        }
        return categories;
    }
    private List generateParentCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            categories.add(new Category().setName("ParentCategory " + i));
        }
        return categories;
    }
    private Map<Category, BigDecimal> generateData() {
        Map<Category, BigDecimal> data = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            Category category = budgetManager.getSubcategories().get(i);
            data.put(category, new BigDecimal(random.nextInt(10)));
        }
        return data;
    }

}
