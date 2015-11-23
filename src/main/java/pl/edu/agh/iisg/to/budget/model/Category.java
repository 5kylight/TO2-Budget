package pl.edu.agh.iisg.to.budget.model;

/**
 * Created by tom on 23.11.15.
 */
public class Category {
    private String name;
    private Category parent;
    private Budget budget;


    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }
}
