package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.property.ObjectProperty;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tom on 16.12.15.
 */
public class BudgetManager implements IBudget{

    @Override
    public Map<Category, BigDecimal> getPlannedAmmountsPerCategory() {
        Map<Category, BigDecimal> plannedBudgetsMap = new HashMap<>();
        for(Budget b : data){
            if(!b.getCategory().get().isParent()){
                plannedBudgetsMap.put(b.getCategory().get(), b.getPlanned().get());
            }
        }
        return plannedBudgetsMap;
    }

    private List<Budget> data;
    private Budget generalBudget;
    private List<Category> subcategories;  // tymczasowo
    private List<Category> parentCategories;

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public List<Category> getParentCategories() {
        return parentCategories;
    }

    public List<Budget> getData() {
        return data;
    }

    public Budget getGeneralBudget() {
        return generalBudget;
    }

    public BudgetManager() {
        generalBudget = new Budget(new BigDecimal(0));
    }

    public BudgetManager(List<Budget> data, List<Category> subcategories, List<Category> parentCategories) {
        this.data = data;
        this.subcategories = subcategories;
        this.parentCategories = parentCategories;
        generalBudget = new Budget(new BigDecimal(0));
        int total = parentCategories.stream().map(this::getBudgetForCategory).mapToInt(budget -> budget.getPlanned().get().intValue()).sum();
        generalBudget.setPlanned(new BigDecimal(total));
    }
    
    public void removeBudget(Budget budget) {
        this.data.remove(budget);
    }

    public void setGeneralBudgetPlan(BigDecimal plan) {
        this.generalBudget.setPlanned(plan);
    }

    public List<Budget> getExpenses(Map<Category, BigDecimal> categorisedExpenses) {
        List<Budget> budgets = new ArrayList<>();
        for (Map.Entry<Category, BigDecimal> entry : categorisedExpenses.entrySet()) {
            Budget budget = new Budget();
            budget.setCategory(entry.getKey());
            budget.setSpent(budget.getSpent().get().add(entry.getValue()));
            generalBudget.setSpent(generalBudget.getSpent().get().add(budget.getSpent().get()));

            budgets.add(budget);
        }
        return budgets;
    }

    public List<Category> getParentCategories(List<Category> categories) {
        return categories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalPlanPerCategory(Category category) {
        int sum = getPlanPerCategory(category);
        if (!category.isParent()) {
            return new BigDecimal(sum);
        } else {
            for(ObjectProperty<Category> categoryOP : category.getSubCategories()){
                sum+=getPlanPerCategory(categoryOP.get());
            }
            return new BigDecimal(sum);
        }
    }

    public int getPlanPerCategory(Category category){
        return data.stream()
                .filter(budget -> budget.getCategory().get().toString().equals(category.getName().get()))
                .mapToInt(budget1 -> budget1.getPlanned().get().intValue()).sum();
    }

    public BigDecimal getTotalSpentPerCategory(Category category) {
        int sum = getSpentPerCategory(category);
        if (!category.isParent()) {
            return new BigDecimal(sum);
        } else {
            for(ObjectProperty<Category> categoryOP : category.getSubCategories()){
                sum+=getSpentPerCategory(categoryOP.get());
            }
            return new BigDecimal(sum);
        }
    }

    public List<Budget> getSubcategoriesBudgets(Category parentCategory) {
        return data.stream().filter(x -> x.getCategory().get().getParent() == parentCategory).collect(Collectors.toList());
    }

    private int getSpentPerCategory(Category category){
        return data.stream()
                .filter(budget -> budget.getCategory().get().toString().equals(category.getName().get()))
                .mapToInt(budget1 -> budget1.getSpent().get().intValue()).sum();
    }

    public BigDecimal getSummarizedBalance(Category category){
        return getTotalPlanPerCategory(category).subtract(getTotalSpentPerCategory(category));
    }

    public Budget getBudgetForCategory(Category category) {
        Optional<Budget> o = this.data.stream().filter(x -> x.getCategory().get().equals(category)).findFirst();
        if (o.isPresent())
            return o.get();
        return null;
    }

    public void addBudget(Budget budget) {
        this.data.add(budget);
    }

    public void addSubcategory(Category category) {
        this.subcategories.add(category);
    }

    public void addParentCategory(Category category) {
        this.parentCategories.add(category);
    }

    public void setData(List<Budget> data) {
        this.data = data;

    }

    public void setParentCategories(List<Category> parentCategories) {
        this.parentCategories = parentCategories;

    }

    public void updateGeneralBudget() {
        int total = parentCategories.stream().map(this::getBudgetForCategory).mapToInt(budget -> budget.getPlanned().get().intValue()).sum();
        generalBudget.setPlanned(new BigDecimal(total));
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
