package pl.edu.agh.iisg.to.budget.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.util.stream.Collectors;

/**
 * Created by tom on 07.12.15.
 */
public class AddCategoryDialogController {
    private static final Logger logger = LogManager.getLogger(AddCategoryDialogController.class);


    private BudgetAppController appController;

    private Category category;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private CheckBox isParentCheckBox;

    private Stage dialogStage;

    private boolean approved;

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }
    public void setCategory(Category category) {
        this.category = category;
    }

    @FXML
    public void initialize() {

    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Category category) {
        this.category = category;

        updateControls();
    }



    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        if (isInputValid()) {
            validate();
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        dialogStage.close();
    }

    private boolean isInputValid() {
        // TODO: implement

        return true;
    }

    private void validate() {
        if (categoryNameTextField.getText() == null)
            logger.error("Category name is to short");
        else {
            String categoryName = categoryNameTextField.getText();
            if (isParentCheckBox.isSelected()) {
                approved = true;
                if (!appController.getParentCategories().stream().filter(x -> x.getName().get().equals(categoryName)).collect(Collectors.toList()).isEmpty()) {
                    logger.error("Parent Category already exists");
                }
            } else {
                approved = false;
                if (!appController.getSubcategories().stream().filter(x -> x.getName().get().equals(categoryName)).collect(Collectors.toList()).isEmpty()) {
                    logger.error("Subcategory already exists");
                }
            }
            category.setName(categoryName);
        }
    }


    private void updateControls() {
        logger.debug("Updating controls");
    }

}
