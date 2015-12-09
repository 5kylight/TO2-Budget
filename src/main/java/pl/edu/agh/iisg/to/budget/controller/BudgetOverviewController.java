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

    public void setGeneralBud(Budget generalBud) {
        this.generalBud = generalBud;
    }

    private Budget generalBud;

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
    private Button refreshButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label generalBalance;

    @FXML
    private Label generalBudget;

    @FXML
    private void initialize() {

        // Main table
        budgetTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getName());

        amountColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAmount());
        spentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getBalance());
        parentNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getParent() == null ? null : dataValue.getValue().getCategory().get().getParent().getName());

        // Buttons
        deleteButton.disableProperty().bind(Bindings.isEmpty(budgetTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty()
                .bind(Bindings.size(budgetTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));

        // ProgressBar
        progressBar.setVisible(true);
    }

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        updateControls();
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        Budget budget = budgetTable.getSelectionModel().getSelectedItem();
        if (budget != null) {
            generalBud.setAmount(generalBud.getAmount().get().subtract(budget.getAmount().get())); //TODO:?? problem described at the end
            generalBud.setSpent(generalBud.getSpent().get().subtract(budget.getSpent().get()));
            budgetTable.getItems().remove(budget);
        }
        updateControls();
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        Budget budget = budgetTable.getSelectionModel().getSelectedItem();
        if (budget != null) {
            appController.showBudgetEditDialog(budget);
            generalBud.setAmount(generalBud.getAmount().get().add(budget.getAmount().get()));    // bardzo brzydko TODO: a tak¿e nie dzia³a poprawnie tak przy okazji...
        }
        updateControls();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Budget budget = new Budget(new BigDecimal(0));
        if (appController.showBudgetEditDialog(budget)) {
            budgetTable.getItems().add(budget);
            generalBud.setAmount(generalBud.getAmount().get().add(budget.getAmount().get()));    // bardzo brzydko
        }

        updateControls();
    }

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Budget> data) {
        this.data = data;
        budgetTable.setItems(FXCollections.observableArrayList(data));
    }

    public void updateControls() {
        generalBalance.setText(generalBud.getBalance().get().toString());
        generalBudget.setText(generalBud.getAmount().get().toString());
        budgetTable.getColumns().get(0).setVisible(false);
        budgetTable.getColumns().get(0).setVisible(true);
    }

/*
    TODO:
        while handling edit action 100->120 - general increase 120 (should 20)(and it should not even increase for me!!
                                                                                AI: -we should keep total planned by user amount elswhere
                                                                                    -we should not subtract amount from subcategory->send it to parentcategory!(better for me))
*/

}
