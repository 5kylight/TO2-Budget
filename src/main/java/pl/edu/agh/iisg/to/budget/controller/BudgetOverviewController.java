package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.model.Budget;
import pl.edu.agh.iisg.to.budget.model.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tom on 23.11.15.
 */
public class BudgetOverviewController {
    private static final Logger logger = LogManager.getLogger(BudgetOverviewController.class);

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
    private TableColumn<Budget, BigDecimal> parentPlannedColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> parentBalanceColumn;


    @FXML
    private TableView<Budget> planTable;

    @FXML
    private TableColumn<Budget, String> categoryNameColumn;

    @FXML
    private TableColumn<Budget, BigDecimal> plannedColumn;

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
        parentPlannedColumn.setCellValueFactory(dataValue -> dataValue.getValue().getPlanned());
        parentSpentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        parentBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getBalance());

        parentPlanTable.setRowFactory( tv -> {
            TableRow<Budget> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Budget parentBudget = row.getItem();
                    if (parentBudget != null)
                        planTable.setItems(FXCollections.observableArrayList(appController.getSubcategoriesBudgets(parentBudget.getCategory().get())));
                }
            });
            return row ;
        });

        // Planned
        planTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getName());
        plannedColumn.setCellValueFactory(dataValue -> dataValue.getValue().getPlanned());
        spentColumn.setCellValueFactory(dataValue -> dataValue.getValue().getSpent());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().getBalance());

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
            generalBud.setPlanned(generalBud.getPlanned().get().subtract(budget.getPlanned().get())); //TODO:?? problem described at the end
            generalBud.setSpent(generalBud.getSpent().get().subtract(budget.getSpent().get()));
            planTable.getItems().remove(budget);
        }
        updateControls();
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        Budget budget = planTable.getSelectionModel().getSelectedItem();
        BigDecimal oldPlanned = budget.getPlanned().get();

        if (budget != null) {
            appController.showBudgetEditDialog(budget);
            generalBud.setPlanned(generalBud.getPlanned().get().subtract(oldPlanned));
            generalBud.setPlanned(generalBud.getPlanned().get().add(budget.getPlanned().get()));    // bardzo brzydko TODO: a tak�e nie dzia�a poprawnie tak przy okazji...

        }
        updateControls();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Budget budget = new Budget(new BigDecimal(0));
        if (appController.showBudgetEditDialog(budget)) {
            planTable.getItems().add(budget);
            generalBud.setPlanned(generalBud.getPlanned().get().add(budget.getPlanned().get()));    // bardzo brzydko
        }

        updateControls();
    }

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Category> parentPlanned) { // List of
//        this.data = parentPlanned;
        parentPlanTable.setItems(FXCollections.observableArrayList(parentPlanned.stream().map(x -> {
            Budget o = appController.getBudgetForCategory(x);
            if (o != null) {
                return o;
            }
            o = new Budget(new BigDecimal(0));
            o.setCategory(x);
            o.setSpent(appController.getTotalSpentPerCategory(x));
            return o;
        }).collect(Collectors.toList())));


        /* Take first's subcategories - */  //
        List<Budget> a = appController.getSubcategoriesBudgets(parentPlanned.get(0));
        if (a != null) {
                planTable.setItems(FXCollections.observableArrayList(a));
        }

    }

    public void updateControls() {
        generalBalance.setText(generalBud.getBalance().get().toString());
        generalBudget.setText(generalBud.getPlanned().get().toString());
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
