package pl.edu.agh.iisg.to.budget.controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tom on 24.11.15.
 */
public class BudgetAppController {

    private Stage stage;

    public BudgetAppController (Stage stage) {
        this.stage = stage;
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
            controller.setGeneralBal(new BigDecimal(300));
            controller.setGeneralBud(new BigDecimal(10));

            controller.setAppController(this);
            List<Budget> data = new ArrayList<>();

            addData(data);
            controller.setData(data);

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    private void addData(List<Budget> data) {
        for (int i = 0; i < 10; i++) {
            data.add(new Budget()
                    .setCategory(new Category()
                            .setName("Spożywcze " + i)
                            .setBudget(new Budget(new BigDecimal(30)))
                            .setParent(new Category().setName("Zakupy " + i)))
                    .setAmount(new BigDecimal(15))
                    .setSpent(new BigDecimal(15%(i+1)))
                    //.setBalance(new BigDecimal(15))
            );
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

            // Set the person into the controller.
            BudgetEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategories(generateCategories());
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
            categories.add(new Category().setName("dupa " + i));
        }
        return categories;
    }
    /*Później w BudgetAppControlerze chciałbym taką metodę która dostaje na wejściu mapę categorii i ile zostało wydane lub przypływu a zwraca listę budzetów
    Wejściem dla niej będzie to co dostaniemy od wydatków i przerobi to na nasze budżety
*/

    public Budget setWholeBudget(BigDecimal amount){
        return new Budget(amount);
    }

    public List<Budget> getExpenses(Map<Category, BigDecimal> categorisedExpenses){
        List<Budget> budgets = new ArrayList<>();
        for (Map.Entry<Category, BigDecimal> entry : categorisedExpenses.entrySet()) {
            Budget budget = new Budget();
            budget.setCategory(entry.getKey());
            budget.setSpent(budget.getSpent().get().add(entry.getValue()));
            budgets.add(budget);
        }
        return budgets;
    }


}
