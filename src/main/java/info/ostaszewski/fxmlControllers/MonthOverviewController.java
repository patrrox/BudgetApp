package info.ostaszewski.fxmlControllers;

import com.sun.org.apache.xalan.internal.utils.XMLSecurityPropertyManager;
import info.ostaszewski.DAO.DAOcontroller;
import info.ostaszewski.Main;
import info.ostaszewski.domain.Expense;
import info.ostaszewski.domain.Month;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;


/**
 * Created by Asus on 29.08.2017.
 */
public class MonthOverviewController {

    @FXML
    private TableView<Month> monthsTable;
    @FXML
    private TableColumn<Month, Integer> yearColumn;
    @FXML
    private TableColumn<Month, String> nameColumn;
    @FXML
    private TableColumn<Month, String> budgetColumn;


    @FXML
    private TableView<Expense> expenseTableView;
    @FXML
    private TableColumn<Expense, LocalDate> dateTableColumn;
    @FXML
    private TableColumn<Expense, String> nameExpenseColumn;
    @FXML
    private TableColumn<Expense, String> categoryColumn;
    @FXML
    private TableColumn<Expense, String> priceColumn;


    @FXML
    private PieChart monthDataPieChart;


    @FXML
    Label restOfYourBudgetLabel;

    @FXML
    Label caption = new Label("");

    private Main main;

    private DAOcontroller dao;

    public MonthOverviewController() {


    }

    @FXML
    private void initialize() {
        System.out.println("INITIALIZE");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().getYearProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        budgetColumn.setCellValueFactory(cellData -> cellData.getValue().getBudgetFormated());

        //listen for selection changes and show
        monthsTable.getSelectionModel().selectedItemProperty().addListener((
                (observable, oldValue, newValue) -> showMonthDetails(newValue)));
        System.out.println("KONIEC INITIALIZE");

    }

    //called when the user clicks on the month
    private void showMonthDetails(Month a) {
        expenseTableView.setItems(a.getExpenseObservableList());

        dateTableColumn.setCellValueFactory(cellData -> cellData.getValue().getLocalDateExpenseProperty());
        nameExpenseColumn.setCellValueFactory(cellData -> cellData.getValue().getNameExpenseProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryExpenseProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceFormated());

        caption.setText("");
        addDataToChart(a);
        restOfYourBudgetCalculator(a);

    }

    @FXML
    private void handleDeleteMonth() {
        int selectedIndex = monthsTable.getSelectionModel().getSelectedIndex();
        Month m = monthsTable.getSelectionModel().getSelectedItem();
        if (selectedIndex >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You will delete all data associated with this month ");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                main.getMonthObservableList().remove(selectedIndex);
                System.out.println(m.getName());
                dao.deleteMonth(m);

            }
        }

    }

    @FXML
    private void handleAddMonth() {
        Month p = new Month("", 0, 0);
        boolean okClicked = main.showMonthAddDialog(p);

        if (okClicked) {

            if (dao.findMonthDuplicate(p.getName(), p.getYear(), p.getBudget()) == false) {
                main.getMonthObservableList().add(p);
                dao.addMonth(p);
            } else {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");
                alert.setHeaderText("You can't duplicate the month");
                alert.showAndWait();
            }
        }

    }

    @FXML
    private void handleEditMonth() {


        Month selectedMonths = monthsTable.getSelectionModel().getSelectedItem();
        if (selectedMonths != null) {
            boolean okClicked = main.showMonthAddDialog(selectedMonths);

            if (okClicked) {
                monthsTable.refresh();
                dao.updateMonth(selectedMonths);
                restOfYourBudgetCalculator(selectedMonths);
            }

        }

    }


    @FXML
    private void handleAddExpense() {
        int selectedIndexM = monthsTable.getSelectionModel().getSelectedIndex();
        System.out.println(selectedIndexM);
        Month month = monthsTable.getSelectionModel().getSelectedItem();
        System.out.println(month.getName());
        Expense e = new Expense("", "", 0.0, LocalDate.now());
        boolean okClicked = main.showExpenseAddDialog(e);

        if (okClicked) {
            main.getMonthObservableList().get(selectedIndexM).getExpenseObservableList().add(e);

            dao.addExpenseToMonth(month.getName(), month.getYear(), month.getBudget(), e);

            addDataToChart(main.getMonthObservableList().get(selectedIndexM));
            restOfYourBudgetCalculator(main.getMonthObservableList().get(selectedIndexM));
        }

    }

    @FXML
    private void handleDeleteExpense() {
        int selectedIndex = monthsTable.getSelectionModel().getSelectedIndex();
        int expIndex = expenseTableView.getSelectionModel().getSelectedIndex();
        Month m = monthsTable.getSelectionModel().getSelectedItem();
        Expense e = expenseTableView.getSelectionModel().getSelectedItem();
        if (selectedIndex >= 0 && expIndex >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("You will delete this expense ");
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                m.getExpenseObservableList().remove(e);
                System.out.println(m.getName());
                dao.deleteExpense(m, e);

                addDataToChart(main.getMonthObservableList().get(selectedIndex));
                restOfYourBudgetCalculator(main.getMonthObservableList().get(selectedIndex));

            }
        }

    }


    public void setMain(Main main) {
        System.out.println("SET MAIN");
        this.main = main;
        //add observable list to data to the table
        monthsTable.setItems(main.getMonthObservableList());
        monthsTable.getSelectionModel().selectLast();
        dao = main.getDao();
        System.out.println("END SET MAIN");


    }


    private void addDataToChart(Month a) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        ObservableList<Expense> e = a.getExpenseObservableList();

        ObservableList<String> categoryListInChoiceBox = FXCollections.observableArrayList("Food", "Entertainment", "Transport", "Home", "Clothing", "Electronics", "Health and beauty", "Children", "Work", "Other");

        Double sum = 0.0;

        for (String s : categoryListInChoiceBox) {

            String cat = s;
            for (Expense exp : e) {
                Expense qw = exp;

                if (cat.equals(qw.getCategory())) {
                    sum = sum + qw.getPrice();

                }
            }
            if (sum != 0.0) {
                pieChartData.add(new PieChart.Data(cat, sum));
            }
            sum = 0.0;

        }

        monthDataPieChart.setTitle("Monthly Expenses");
        monthDataPieChart.setData(pieChartData);


        // caption.setTextFill(Color.DARKORANGE);
        //  caption.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : monthDataPieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setTranslateX(event.getX());
                    caption.setTranslateY(event.getY() - 200);
                    double percentage = (double) ((data.getPieValue() * 100) / a.getBudget());
                    String s = convertToFormateBudget(percentage);
                    caption.setText(String.valueOf(convertToFormate(data.getPieValue())) + " | " + s);


                }
            });
        }

        for (final PieChart.Data data : monthDataPieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    caption.setTranslateX(event.getX());
                    caption.setTranslateY(event.getY() - 200);
                    caption.setText(String.valueOf(""));


                }
            });
        }

    }


    private void restOfYourBudgetCalculator(Month a) {
        ObservableList<Expense> expensesListAll = a.getExpenseObservableList();
        double sum = 0;
        for (Expense e : expensesListAll) {
            sum += e.getPrice();
        }
        double dateToExport = a.getBudget() - sum;

        String x = convertToFormate(dateToExport);
        restOfYourBudgetLabel.setText(x);
    }

    public String setRestOfYourBudgetCalculator(Month a)
    {
        ObservableList<Expense> expensesListAll = a.getExpenseObservableList();
        double sum = 0;
        for (Expense e : expensesListAll) {
            sum += e.getPrice();
        }
        double dateToExport = a.getBudget() - sum;

        return convertToFormate(dateToExport);

    }



    private String convertToFormate(double price) {
        String q = String.format("%.2f", price);
        q = q + " " + main.getCurrency();
        return (q);
    }

    private String convertToFormateBudget(double price) {
        String q = String.format("%.0f", price);
        q = q + " " + "%";
        return (q);
    }

}
