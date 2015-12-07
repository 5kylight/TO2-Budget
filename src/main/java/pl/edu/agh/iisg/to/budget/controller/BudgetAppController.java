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
            BorderPane rootLayout = (BorderPane) loader.load();

            // set initial data into controller
            BudgetOverviewController controller = loader.getController();
            controller.setAppController(this);
            List<Budget> data = new ArrayList<>();

            addData(data);
//            data.add(new Transaction()
//                    .setCategory(new Category()
//                            .setName("Zycie")
//                            .setBudget(new Budget(new BigDecimal(30)))
//                            .setParent(new Category().setName("Dupa")))
//                    .setLocalTime(LocalTime.now())
//                    .setName("Mleko")
//                    .setIsDone(new Boolean(true))
//                    .setValue(new BigDecimal(13)));
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
        for (int i = 0; i < 100; i++) {
            data.add(new Budget()
                    .setCategory(new Category()
                            .setName("Spożywcze")
                            .setBudget(new Budget(new BigDecimal(30)))
                            .setParent(new Category().setName("Zakupy")))
                    .setAmount(new BigDecimal(15))
                    .setBalance(new BigDecimal(15))
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
            controller.setData(budget);
            controller.setCategories(generateCategories());

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
        for (int i = 0; i < 100; i++) {
            categories.add(new Category().setName("dupa"));
        }
        return categories;
    }



}
