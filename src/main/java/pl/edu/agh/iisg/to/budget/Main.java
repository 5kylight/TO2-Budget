package pl.edu.agh.iisg.to.budget;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.iisg.to.budget.controller.BudgetAppController;

/**
 * Created by tom on 23.11.15.
 */
public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    private Stage primaryStage;

    private BudgetAppController presenter;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Budget");

        this.presenter = new BudgetAppController(primaryStage);
        this.presenter.initRootLayout();

    }

    public static void main(String[] args) {

        logger.info("Main started");
        launch(args);
    }
}
