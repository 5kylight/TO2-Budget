package pl.edu.agh.iisg.to.budget.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pl.edu.agh.iisg.to.budget.model.Category;
import pl.edu.agh.iisg.to.budget.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tom on 23.11.15.
 */
public class BudgetOverviewController {
    private BudgetAppController appController;
    private List<Transaction> data;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Category> categoryColumn;

    @FXML
    private TableColumn<Transaction, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Transaction, String> parentNameColumn;

    @FXML
    private TableColumn<Transaction, String> nameColumn;

    @FXML
    private TableColumn<Transaction, Boolean> isDoneColumn;

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

        transactionsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory());
        amountColumn.setCellValueFactory(dataValue -> dataValue.getValue().getValue());
        parentNameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getCategory().get().getParent().getName());
        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().getName());
        isDoneColumn.setCellValueFactory(dataValue -> dataValue.getValue().getIsDone());

        deleteButton.disableProperty().bind(Bindings.isEmpty(transactionsTable.getSelectionModel().getSelectedItems()));

        editButton.disableProperty()
                .bind(Bindings.size(transactionsTable.getSelectionModel().getSelectedItems()).isNotEqualTo(1));

        progressBar.setVisible(false);
    }

    @FXML
    private void handleDeleteAction(ActionEvent event) {

    }

    @FXML
    private void handleEditAction(ActionEvent event) {

    }

    @FXML
    private void handleAddAction(ActionEvent event) {

    }

    public void setAppController(BudgetAppController appController) {
        this.appController = appController;
    }

    public void setData(List<Transaction> data) {
        this.data = data;
        transactionsTable.setItems(FXCollections.observableArrayList(data));
    }
}