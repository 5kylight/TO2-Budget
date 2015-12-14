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
    private TableView<Budget> parentPlanTable;

    @FXML
    private TableColumn<Budget, BigDecimal> parentSpentColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> parentAmountColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> parentBalanceColumn;


    @FXML
    private TableView<Budget> planTable;

    @FXML
    private TableColumn<Budget, String> categoryNameColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> spentColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> balanceColumn;

    @FXML
    private TableColumn<Budget, String> parentCategoryName;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField monthlyPlanedBudget;
    
    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label generalBalance;

    @FXML
    private Label generalBudget;

    @FXML
    private void initialize() {

        // Parent Planned
        parentPlanTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        parentCategoryName.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getName());
        parentAmountColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAmount());
        parentSpentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        parentBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getBalance());

        parentPlanTable.setRowFactory( tv -> {
            TableRow<Budget> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Budget parentBudget = row.getItem();
                    System.out.println(parentBudget);
                    planTable.setItems(FXCollections.observableArrayList(parentBudget.getSubcategoriesBudgets()));
                }
            });
            return row ;
        });

        // Planned
        planTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getName());
        amountColumn.setCellValueFactory(dataValue -> dataValue.getValue().getAmount());
        spentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getBalance());
//        parentNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getParent() == null ? null : dataValue.getValue().getCategory().get().getParent().getName());

        // Buttons
        deleteButton.disableProperty().bind(Bindings.isEmpty(planTable.getSelectionModel().getSelectedItems()));
        editButton.disableProperty()
                .bind(Bindings.size(planTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));

        // ProgressBar
        progressBar.setVisible(true);
    }

    @FXML
    private void handleRefreshAction(ActionEvent event) {
        updateControls();
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        Budget budget = planTable.getSelectionModel().getSelectedItem();
        if (budget != null) {
            generalBud.setAmount(generalBud.getAmount().get().subtract(budget.getAmount().get())); //TODO:?? problem described at the end
            generalBud.setSpent(generalBud.getSpent().get().subtract(budget.getSpent().get()));
            planTable.getItems().remove(budget);
        }
        updateControls();
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        Budget budget = planTable.getSelectionModel().getSelectedItem();
        BigDecimal oldAmount = budget.getAmount().get();

        if (budget != null) {
            appController.showBudgetEditDialog(budget);
            generalBud.setAmount(generalBud.getAmount().get().subtract(oldAmount));
            generalBud.setAmount(generalBud.getAmount().get().add(budget.getAmount().get()));    // bardzo brzydko TODO: a tak�e nie dzia�a poprawnie tak przy okazji...

        }
        updateControls();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Budget budget = new Budget(new BigDecimal(0));
        if (appController.showBudgetEditDialog(budget)) {
            planTable.getItems().add(budget);
            generalBud.setAmount(generalBud.getAmount().get().add(budget.getAmount().get()));    // bardzo brzydko
        }

        updateControls();
    }

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Budget> parentPlanned) { // List of
        this.data = parentPlanned;
        parentPlanTable.setItems(FXCollections.observableArrayList(parentPlanned));
        planTable.setItems(FXCollections.observableArrayList(parentPlanned.get(0).getSubcategoriesBudgets()));
    }

    public void updateControls() {
        generalBalance.setText(generalBud.getBalance().get().toString());
        generalBudget.setText(generalBud.getAmount().get().toString());
        planTable.getColumns().get(0).setVisible(false);
        planTable.getColumns().get(0).setVisible(true);
    }

/*
    TODO:
        while handling edit action 100->120 - general increase 120 (should 20)(and it should not even increase for me!!
                                                                                AI: -we should keep total planned by user amount elswhere
                                                                                    -we should not subtract amount from subcategory->send it to parentcategory!(better for me))
*/

}
