package info.ostaszewski;


import info.ostaszewski.DAO.DAOcontroller;

import info.ostaszewski.domain.Expense;
import info.ostaszewski.domain.Month;
import info.ostaszewski.fxmlControllers.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Asus on 22.08.2017.
 */

public class Main extends Application {


    private Stage primaryStage;
    private BorderPane rootLayout;
    DAOcontroller dao = new DAOcontroller();
    public List<Month> months = new ArrayList<Month>();
    private ObservableList<Month> monthObservableList = FXCollections.observableArrayList();
    public String currency = "zł";

    public Main() {


    }

    public List<Month> getMonths() {
        return months;
    }

    public ObservableList<Month> getMonthObservableList() {
        return monthObservableList;
    }

    public DAOcontroller getDao() {
        return dao;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // List<Month>m=dao.findAll();
        monthObservableList = dao.findAllObservable();

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("BudgetApp");
   //     primaryStage.getIcons().add(new Image("/icons/pie-chart.png"));

        initRootLayout();
        showMonthOverview();


        primaryStage.setOnCloseRequest(event -> {
            System.out.println("App -> close");
            System.out.println("Database -> exit");
            dao.exitFromDataBase();
        });


    }

    /**
     * Initializes the root layout
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();


            RootLayoutController controller = loader.getController();
            controller.setMain(this);


            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the month overview inside the root layout
     */
    public void showMonthOverview() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/MonthOverview.fxml"));
            AnchorPane monthOverview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(monthOverview);

            //give the controller access to main app
            MonthOverviewController controller = loader.getController();
            controller.setMain(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean showMonthAddDialog(Month m) {
        //Load the fxml file and create a new stage for popop dialog
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/MonthEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Creare the dialog Stage;
            Stage dialogStage = new Stage();
            dialogStage.setTitle("add Month");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            MonthEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMonth(m);
            //Add new Month
            //.....

            //Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked(); /////
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showExpenseAddDialog(Expense ex) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/ExpenseEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Creare the dialog Stage;
            Stage dialogStage = new Stage();
            dialogStage.setTitle("add Month");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ExpenseEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setExpense(ex);
            //Add new Month
            //.....

            //Show the dialog and wait until the user closes it
            dialogStage.showAndWait();
            return controller.isOkClicked(); /////
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showGeneralStats() {
        System.out.println("stats");
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/StatsGeneral.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //Creare the dialog Stage;
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Stats");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StatsGeneralController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMonthObservableList(monthObservableList);
            dialogStage.showAndWait();


        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void exit(){
        dao.exitFromDataBase();
        System.exit(0);
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

