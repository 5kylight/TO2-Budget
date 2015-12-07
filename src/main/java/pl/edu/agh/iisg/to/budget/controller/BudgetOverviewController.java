package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.iisg.to.budget.model.Budget;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tom on 23.11.15.
 */
public class BudgetOverviewController {
    private BudgetAppController appController;
    private List<Budget> data;

//    @FXML
//    private Accordion accordion;

    @FXML
    private TableView<Budget> budgetTable;

    @FXML
    private TableColumn<Budget, String> categoryNameColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> spentColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> balanceColumn;

    @FXML
    private TableColumn<Budget, String> parentNameColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private void initialize() {

        // Main table
        budgetTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getName());

        amountColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAmount());
        spentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        parentNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getParent().getName());

        // Buttons
        deleteButton.disableProperty().bind(Bindings.isEmpty(budgetTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty()
                .bind(Bindings.size(budgetTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));

        // ProgressBar
        progressBar.setVisible(true);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {

    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        Budget budget = budgetTable.getSelectionModel().getSelectedItem();
        if (budget != null) {
            appController.showBudgetEditDialog(budget);
        }
    }

    @FXML
    private void handleAddAction(ActionEvent event) {

    }

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Budget> data) {
        this.data = data;
        budgetTable.setItems(FXCollections.observableArrayList(data));
    }
}
