package info.ostaszewski.fxmlControllers;

import info.ostaszewski.domain.Month;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * Created by Asus on 14.02.2018.
 */
public class StatsGeneralController {

    private Stage dialogStage;
    private ObservableList<Month> monthObservableList = FXCollections.observableArrayList();


    public void setMonthObservableList(ObservableList<Month> monthObservableList)
    {
        this.monthObservableList=FXCollections.observableArrayList(monthObservableList);
        for (int i=0;i<monthObservableList.size();i++)
        {
            System.out.println("miesiac");
        }
    }


    @FXML
    private void initialize() {

    }




    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
