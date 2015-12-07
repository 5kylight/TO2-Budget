package pl.edu.agh.iisg.to.budget.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

/**
 * Created by tom on 07.12.15.
 */
public class BudgetEditDialogController {
    private Budget budget;

    private ObservableList<Category> categories;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField amountTextField;


    private Stage dialogStage;

    private boolean approved;


    @FXML
    public void initialize() {

    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setData(Budget budget) {
        this.budget = budget;
        updateControls();
    }

    public void setCategories(List categories) {
        this.categories = FXCollections.observableArrayList(categories);
    }

    public boolean isApproved() {
        return approved;
    }

    @FXML
    private void handleOkAction(ActionEvent event) {
        if (isInputValid()) {
            updateModel();
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

    private void updateModel() {
        DecimalFormat decimalFormatter = new DecimalFormat();
        decimalFormatter.setParseBigDecimal(true);

        budget.setCategory(categoryComboBox.getValue());
        try {
            budget.setSpent((BigDecimal) decimalFormatter.parse(amountTextField.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//
//        transaction.setDate(converter.fromString(dateTextField.getText()));
//        transaction.setPayee(payeeTextField.getText());
//        transaction.setCategory(new Category(categoryTextField.getText()));
//        transaction.setMemo(memoTextField.getText());
//
//        try {
//            transaction.setOutflow((BigDecimal) decimalFormatter.parse(outflowTextField.getText()));
//            transaction.setInflow((BigDecimal) decimalFormatter.parse(inflowTextField.getText()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

    }

    private void updateControls() {

        amountTextField.setText(budget.getAmount().getValue().toString());
        categoryComboBox.getItems().addAll(this.categories);
//        payeeTextField.setText(transaction.getPayee());
//        categoryTextField.setText(transaction.getCategory().getName());
//        memoTextField.setText(transaction.getMemo());
//        outflowTextField.setText(transaction.getOutflow().toString());
//        inflowTextField.setText(transaction.getInflow().toString());
    }


}
