package pl.edu.agh.iisg.to.budget.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;
import pl.edu.agh.iisg.to.budget.model.CategoryConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tom on 07.12.15.
 */
public class BudgetEditDialogController {
    private static final Logger logger = LogManager.getLogger(BudgetEditDialogController.class);


    private BudgetAppController appController;
    private Budget budget;

    private ObservableList<Category> categories;
    private ObservableList<Category> parentCategories;
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private ComboBox<Category> parentCategoryComboBox;

    @FXML
    private TextField planTextField;

    private Stage dialogStage;

    private boolean approved;


    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    @FXML
    public void initialize() {
        categoryComboBox.setConverter(new CategoryConverter());
        parentCategoryComboBox.setConverter(new CategoryConverter());
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Budget budget) {
        this.budget = budget;
        if (budget.getCategory() != null) {
            this.categoryComboBox.getSelectionModel().select(budget.getCategory().get());

            if (budget.getCategory().get().getParent() != null)
                this.parentCategoryComboBox.getSelectionModel().select(budget.getCategory().get().getParent());
        }
        updateControls();
    }

    public void setCategories(List<Category> categories) {
        this.categories = FXCollections.observableArrayList();
        this.categories.addAll(categories.stream().filter(x -> appController.getBudgetForCategory(x) == null).collect(Collectors.toList()));

    }
    public void setParentCategories(List<Category> parentCategories) {
        this.parentCategories = FXCollections.observableArrayList();
        this.parentCategories.addAll(parentCategories);
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            validate();
            approved = true;
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
        logger.debug("Updating model");
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setParseBigDecimal(true);

        if (budget.getCategory() != null) {
            budget.getCategory().get().getParent().removeSubcategory(budget.getCategory().get());
        }

        if (categoryComboBox.getValue() == null || parentCategoryComboBox.getValue() == null )
            logger.error("Not both categories selected");


        budget.setCategory(categoryComboBox.getValue());
        budget.getCategory().get().setParent(parentCategoryComboBox.getValue());
        budget.getCategory().get().getParent().addSubCategories(budget.getCategory().get());
        try {
            budget.setPlanned((BigDecimal) decimalFormatter.parse(planTextField.getText()));
        } catch (ParseException e) {
            logger.error(e);
            e.printStackTrace();
        }



//
//        /* Remember old parent category */
//        Category oldParentCategory = null;
//        if (budget.getCategory() != null)
//            oldParentCategory = budget.getCategory().get().getParent();
//
//        if (categoryComboBox.getValue() == null ) {
//            /* Adding parent category */
//            if (parentCategoryComboBox.getValue() == null)
//                logger.error("Cannot add parent category, please specify parent category");
//            budget.setCategory(parentCategoryComboBox.getValue());
//        } else {
//            /* Adding subcategory*/
//
//                budget.setCategory(categoryComboBox.getValue());
//                try {
//                    budget.setPlanned((BigDecimal) decimalFormatter.parse(planTextField.getText()));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Category newParentCategory = parentCategoryComboBox.getValue();
//
//                if (newParentCategory != null) {
//                    if (newParentCategory.equals(oldParentCategory)) {
//                        Budget parentBudget = appController.getBudgetForCategory(newParentCategory);
//                        parentBudget
//
//                    } else {
//                        newParentCategory.addSubCategories(budget.getCategory().get());
//                        budget.getCategory().get().setParent(newParentCategory);
//                    }
//
//                } else {
//                    logger.error("Category must have a parent");
//                }
//        }
    }

    private void updateControls() {
        logger.debug("Updating controls");
        planTextField.setText(budget.getPlanned().getValue().toString());
        categoryComboBox.setItems(this.categories);
        parentCategoryComboBox.setItems(this.parentCategories);
    }




    public List<Category> getParentCategories() {
        return parentCategories;
    }
}
