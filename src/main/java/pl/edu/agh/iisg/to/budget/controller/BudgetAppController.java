package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.Main;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tom on 24.11.15.
 */
public class BudgetAppController {
    private static final Logger logger = LogManager.getLogger(BudgetAppController.class);

    private Stage stage;
    private List<Budget> data;

    Random random = new Random();
    public BudgetAppController(Stage stage) {
        this.stage = stage;
    }

    private Budget generalBudget;

    private List<Category> subcategories;  // tymczasowo

    private List<Category> parentCategories;

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public List<Category> getParentCategories() {
        return parentCategories;
    }
    public void initRootLayout() {
        try {
            this.stage.setTitle("Budget");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BudgetOverviewPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            BudgetOverviewController controller = loader.getController();

            parentCategories = generateParentCategories();
            subcategories = generateSubcategories();
            generalBudget = new Budget(new BigDecimal(0));

            data = getExpenses(generateData());
            //TODO: test data
            for(Budget budget : data){
                budget.setPlanned(budget.getSpent().get().add(new BigDecimal(random.nextInt(10))));
            }

            controller.setAppController(this);
            controller.setData(parentCategories, data);

            int total = parentCategories.stream().map(this::getBudgetForCategory).mapToInt(budget -> budget.getPlanned().get().intValue()).sum();
            generalBudget.setPlanned(new BigDecimal(total));

            controller.setGeneralPla(generalBudget);

            controller.setAppController(this);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    public boolean showBudgetEditDialog(Budget budget) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BudgetEditDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit budget");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BudgetEditDialogController controller = loader.getController();
            controller.setAppController(this);
            controller.setDialogStage(dialogStage);
            controller.setCategories(subcategories);
            controller.setParentCategories(parentCategories);
            controller.setData(budget);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showAddCategoryDialog(Category category) {
        try {
            // Load the fxml file and create a new stage for the dialog
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AddCategoryDialog.fxml"));
            BorderPane page = (BorderPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Category");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddCategoryDialogController controller = loader.getController();
            controller.setAppController(this);
            controller.setDialogStage(dialogStage);
            controller.setCategory(category);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
        Optional<Budget> o = data.stream().filter(x -> x.getCategory().get().equals(category)).findFirst();
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

/**
 *  Generators */

    private List generateSubcategories() {
        List<Category> categories = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Category parentCategory = parentCategories.get(random.nextInt(parentCategories.size()));
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
            Category category = subcategories.get(i);
            data.put(category, new BigDecimal(random.nextInt(10)));
        }
        return data;
    }
}
