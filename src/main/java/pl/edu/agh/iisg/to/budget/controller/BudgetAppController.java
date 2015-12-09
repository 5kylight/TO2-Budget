package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

    private Stage stage;
    private List<Budget> data;

    Random random = new Random();
    public BudgetAppController(Stage stage) {
        this.stage = stage;
    }

    private Budget generalBudget;

    private List<Category> allCategories;  // tymczasowo

    public void initRootLayout() {
        try {
            this.stage.setTitle("Budget");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BudgetOverviewPane.fxml"));
            BorderPane rootLayout = loader.load();

            // set initial data into controller
            BudgetOverviewController controller = loader.getController();

            allCategories = generateCategories();
            generalBudget = new Budget(new BigDecimal(0));

            data = getExpenses(addData());
            //TODO: test data
            for(Budget budget : data){
                budget.setAmount(budget.getSpent().get().add(new BigDecimal(random.nextInt(100))));
            }

            controller.setData(data);

            int total = data.stream().mapToInt(budget -> budget.getAmount().get().intValue()).sum();
            generalBudget.setAmount(new BigDecimal(total));

            controller.setGeneralBud(generalBudget);

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
            controller.setDialogStage(dialogStage);
            allCategories = generateCategories();
            controller.setCategories(allCategories);
            controller.setData(budget);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isApproved();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List generateCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(new Category().setName("Category " + i));
        }
        return categories;
    }

    /*Później w BudgetAppControlerze chciałbym taką metodę która dostaje na wejściu mapę categorii i ile zostało wydane lub przypływu a zwraca listę budzetów
    Wejściem dla niej będzie to co dostaniemy od wydatków i przerobi to na nasze budżety
*/

    public void setGeneralBudgetAmount(BigDecimal amount) {
        this.generalBudget.setAmount(amount);
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

    private Map<Category, BigDecimal> addData() {
        Map<Category, BigDecimal> data = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            Category category = allCategories.get(random.nextInt(allCategories.size() - 1));
            Category parentCategory = allCategories.get(random.nextInt(allCategories.size() - 1));

            category.setParent(parentCategory);
            parentCategory.addSubCategories(category);
            data.put(category, new BigDecimal(random.nextInt(100)));
        }
        return data;
    }

    public List<Category> getParentCategories(List<Category> categories) {
        return categories.stream()
                .filter(category -> category.getParent() == null)
                .collect(Collectors.toList());
    }

    private BigDecimal getTotalAmountPerCategory(Category category) {
        int sum = getAmountPerCategory(category);
        if (!category.isParent()) {
            return new BigDecimal(sum);
        } else {
            for(ObjectProperty<Category> categoryOP : category.getSubCategories()){
                sum+=getAmountPerCategory(categoryOP.get());
            }
            return new BigDecimal(sum);
        }
    }

    private int getAmountPerCategory(Category category){
        return data.stream()
                .filter(budget -> budget.getCategory().get().toString().equals(category.getName().get()))
                .mapToInt(budget1 -> budget1.getAmount().get().intValue()).sum();
    }

    private BigDecimal getTotalSpentPerCategory(Category category) {
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

    private int getSpentPerCategory(Category category){
        return data.stream()
                .filter(budget -> budget.getCategory().get().toString().equals(category.getName().get()))
                .mapToInt(budget1 -> budget1.getSpent().get().intValue()).sum();
    }

    public BigDecimal getSummarizedBalance(Category category){
        return getTotalAmountPerCategory(category).subtract(getTotalSpentPerCategory(category));
    }


}
