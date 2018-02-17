package info.ostaszewski.fxmlControllers;

import info.ostaszewski.domain.Expense;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Asus on 31.08.2017.
 */
public class ExpenseEditDialogController {

    ObservableList<String> categoryListInChoiceBox = FXCollections.observableArrayList("Food", "Entertainment", "Transport", "Home", "Clothing", "Electronics", "Health and beauty", "Children", "Work", "Other");

    @FXML
    private DatePicker dateDatePicker;
    @FXML
    private TextField nameTextField;
    @FXML
    private ChoiceBox categoryChoiceBox;
    @FXML
    private TextField priceTextField;

    private boolean okClicked = false;
    private int indexofCategory = 0;
    private Stage dialogStage;
    private Expense expense;

    @FXML
    private void initialize() {
        categoryChoiceBox.setValue("1");
        categoryChoiceBox.setItems(categoryListInChoiceBox);

        categoryChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println(newValue);
                indexofCategory = (Integer) newValue;
                System.out.println("numer category" + categoryListInChoiceBox.get(indexofCategory));
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;

        categoryChoiceBox.setValue(expense.getCategory());
        dateDatePicker.setValue(expense.getDate());
        nameTextField.setText(expense.getName());
        priceTextField.setText(Double.toString(expense.getPrice()));

    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    public void handleOk() {
        if (isInputValid()) {
            expense.setLocalDate(dateDatePicker.getValue());
            expense.setName(nameTextField.getText());
            expense.setCategory(categoryListInChoiceBox.get(indexofCategory));
            expense.setPrice(Double.parseDouble(priceTextField.getText()));
            expense.setPriceFormated(Double.parseDouble(priceTextField.getText()));
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
        if (priceTextField.getText() == null || priceTextField.getText().length() == 0) {
            errorMesage += "No valid price!\n";
        } else {
            try {
                Double.parseDouble(priceTextField.getText());
            } catch (NumberFormatException e) {
                errorMesage += "No valid price (must be an integer)!\n";
            }
        }
        if (nameTextField.getText() == null || nameTextField.getText().length() == 0) {
            errorMesage += "No valid name";
        }

        if (errorMesage.length() == 0) {
            return true;
        } else return false;
    }

}


