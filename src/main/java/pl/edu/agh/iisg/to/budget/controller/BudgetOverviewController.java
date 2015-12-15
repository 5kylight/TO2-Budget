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
//    private List<Budget> data;

    public void setGeneralPla(Budget generalPla) {
        this.generalPla = generalPla;
    }

    private Budget generalPla;

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
    private Label generalPlan;

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
        logger.info("Refreshing view");
        updateControls();
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {
        Budget budget = planTable.getSelectionModel().getSelectedItem();
        if (budget != null) {
            generalPla.setPlanned(generalPla.getPlanned().get().subtract(budget.getPlanned().get())); //TODO:?? problem described at the end
            generalPla.setSpent(generalPla.getSpent().get().subtract(budget.getSpent().get()));
            Budget parentBudget = appController.getBudgetForCategory(budget.getCategory().get().getParent());
            parentBudget.setPlanned(parentBudget.getPlanned().get().subtract(budget.getPlanned().get())); //TODO:?? problem described at the end
            parentBudget.setSpent(parentBudget.getSpent().get().subtract(budget.getSpent().get()));
            parentBudget.getCategory().get().removeSubcategory(budget.getCategory().get());
            appController.removeBudget(budget);
            planTable.getItems().remove(budget);
        }
        updateControls();
    }

    @FXML
    private void handleEditAction(ActionEvent event) {
        Budget budget = planTable.getSelectionModel().getSelectedItem();
        BigDecimal oldPlanned = budget.getPlanned().get();
        Category oldParentCategory = budget.getCategory().get().getParent();

        appController.showBudgetEditDialog(budget);

        generalPla.setPlanned(generalPla.getPlanned().get().subtract(oldPlanned));
        generalPla.setPlanned(generalPla.getPlanned().get().add(budget.getPlanned().get()));    // bardzo brzydko TODO: a tak�e nie dzia�a poprawnie tak przy okazji...

        Budget oldParentBudget = appController.getBudgetForCategory(oldParentCategory);
        oldParentBudget.setPlanned(oldParentBudget.getPlanned().get().subtract(oldPlanned));
        oldParentBudget.setSpent(oldParentBudget.getSpent().get().subtract(budget.getSpent().get()));
        Budget parentBudget = appController.getBudgetForCategory(budget.getCategory().get().getParent());
        if (parentBudget == null) {
            parentBudget = new Budget(new BigDecimal(0));
            parentBudget.setPlanned(new BigDecimal(0));
            parentBudget.setSpent(new BigDecimal(0));
            appController.addBudget(parentBudget);
        }

        parentBudget.setSpent(parentBudget.getSpent().get().add(budget.getSpent().get()));
        parentBudget.setPlanned(parentBudget.getPlanned().get().add(budget.getPlanned().get()));

        updateControls();
    }

    @FXML
    private void handleAddAction(ActionEvent event) {
        Budget budget = new Budget(new BigDecimal(0));
        if (appController.showBudgetEditDialog(budget)) {
            appController.addBudget(budget);
            Category parentCategory = budget.getCategory().get().getParent();
            Budget parentBudget = null;
            parentBudget = appController.getBudgetForCategory(parentCategory);
            if (parentBudget == null) {
                parentBudget = new Budget(new BigDecimal(0));
                parentBudget.setPlanned(new BigDecimal(0));
                parentBudget.setSpent(new BigDecimal(0));
                appController.addBudget(parentBudget);
            }

            parentBudget.setSpent(parentBudget.getSpent().get().add(budget.getSpent().get()));
            parentBudget.setPlanned(parentBudget.getPlanned().get().add(budget.getPlanned().get()));

            generalPla.setPlanned(generalPla.getPlanned().get().add(budget.getPlanned().get()));
        }

        updateControls();
    }

    @FXML
    private void handleAddCategoryAction() {
        Category category = new Category();
        if (appController.showAddCategoryDialog(category)) {
            appController.addParentCategory(category);
        } else {
            appController.addSubcategory(category);
        }

        updateControls();
    }


    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Category> parentPlanned, List<Budget> data) { // List of
//        this.data = data;
        updateParentPlanTable(parentPlanned);
        /* Take first's subcategories - */  //
        List<Budget> a = appController.getSubcategoriesBudgets(parentPlanned.get(0));
        if (a != null) {
            planTable.setItems(FXCollections.observableArrayList(a));
        }
    }

    public void updateControls() {
        updateParentPlanTable(appController.getParentCategories());
        generalBalance.setText(generalPla.getBalance().get().toString());
        generalPlan.setText(generalPla.getPlanned().get().toString());
        planTable.getColumns().get(0).setVisible(false);
        planTable.getColumns().get(0).setVisible(true);
        parentPlanTable.getColumns().get(0).setVisible(false);
        parentPlanTable.getColumns().get(0).setVisible(true);
    }

    public void updateParentPlanTable(List<Category> categories) {
        parentPlanTable.setItems(FXCollections.observableArrayList(categories.stream().map(x -> {
            Budget o = appController.getBudgetForCategory(x);
            if (o != null) {
                return o;
            }
            o = new Budget(new BigDecimal(0));
            o.setCategory(x);
            o.setSpent(appController.getTotalSpentPerCategory(x));
            o.setPlanned(appController.getTotalPlanPerCategory(x));
            appController.addBudget(o);
            return o;
        }).collect(Collectors.toList())));
    }

/*
    TODO:
        while handling edit action 100->120 - general increase 120 (should 20)(and it should not even increase for me!!
                                                                                AI: -we should keep total planned by user amount elswhere
                                                                                    -we should not subtract amount from subcategory->send it to parentcategory!(better for me))
*/

}
