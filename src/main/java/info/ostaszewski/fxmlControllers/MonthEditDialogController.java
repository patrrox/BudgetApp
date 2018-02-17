package info.ostaszewski.fxmlControllers;

import info.ostaszewski.domain.Month;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.lang.ref.PhantomReference;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asus on 31.08.2017.
 */
public class MonthEditDialogController {
    ObservableList<String> monthListInChoiceBox = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    ObservableList<Integer> yearListInChoiceBox = FXCollections.observableArrayList(2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030);

    @FXML
    private ChoiceBox yearChoiceBox;
    @FXML
    private ChoiceBox monthChoiceBox;
    @FXML
    private TextField budgetTextField;

    private boolean okClicked = false;
    private Stage dialogStage;
    private Month months;
    private int indexofYear = (Calendar.getInstance().get(Calendar.YEAR))%2017;
    private int indexofMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int dayChoiceInt = Calendar.getInstance().get(Calendar.MONTH);
  //  private String s = monthListInChoiceBox

    @FXML
    private void initialize() {


        yearChoiceBox.setItems(yearListInChoiceBox);
        yearChoiceBox.setValue(yearListInChoiceBox.get(indexofYear));

        monthChoiceBox.setItems(monthListInChoiceBox);
        monthChoiceBox.setValue(monthListInChoiceBox.get(dayChoiceInt));


        yearChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //months.setYear(yearListInChoiceBox.get((Integer) newValue));
                // System.out.print(months.getYear());
                System.out.println(newValue);
                indexofYear = (Integer) newValue;
                System.out.println("numer" + indexofYear);
            }
        });

        monthChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue);
                indexofMonth = (Integer) newValue;
                System.out.println("numer miesiaca" + monthListInChoiceBox.get(indexofMonth));
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    public void setMonth(Month months) {
        this.months = months;
        yearChoiceBox.setValue(months.getYear());
        monthChoiceBox.setValue(months.getName());
        budgetTextField.setText(Double.toString(months.getBudget()));
    }

    public boolean isOkClicked() {
        return okClicked;
    }


    @FXML
    public void handleOk() {

        if (isInputValid()) {
            months.setYear(yearListInChoiceBox.get(indexofYear));
            months.setName(monthListInChoiceBox.get(indexofMonth));
            months.setBudget(Double.parseDouble(budgetTextField.getText()));
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCanel() {
        dialogStage.close();
    }


    private boolean isInputValid() {
        String errorMesage = "";
        if (budgetTextField.getText() == null || budgetTextField.getText().length() == 0) {
            errorMesage += "No valid budget!\n";
        } else {
            try {
                Double.parseDouble(budgetTextField.getText());
            } catch (NumberFormatException e) {
                errorMesage += "No valid postal code (must be an double)!\n";
            }
        }
        if (errorMesage.length() == 0) {
            return true;
        } else return false;
    }


}
