package info.ostaszewski.fxmlControllers;

import info.ostaszewski.Main;
import info.ostaszewski.domain.Month;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Created by Asus on 14.02.2018.
 */
public class StatsGeneralController {


    @FXML
    private BarChart<String,Double> barChart;

    @FXML
    private CategoryAxis xAxis;

    private Main main;


    private Stage dialogStage;
    private ObservableList<Month> monthObservableList = FXCollections.observableArrayList();



    public StatsGeneralController()
    {

    }

    public StatsGeneralController(Main main)
    {
        System.out.println("konstruktor");
        this.main = main;
        start();
    }


    public void setMonthObservableList(ObservableList<Month> monthObservableList)
    {

        System.out.println("dlugosc z maina podczas wprowadzania danych: " + main.getMonthObservableList().size() );
        System.out.println("wprowadzanie danych");
        this.monthObservableList=FXCollections.observableArrayList(monthObservableList);
        for (int i=0;i<monthObservableList.size();i++)
        {
            System.out.println("miesiac");
        }
        for (int i=0;i<this.monthObservableList.size();i++)
        {
            System.out.println("miesiac-nowy");
        }
    }


    @FXML
    private void initialize()
    {
        System.out.println("INITIALIZE");
       // showRestOfBudgetBarChart();
        System.out.println("KONIEC INITIALIZE");
    }

    public void start() {

        showRestOfBudgetBarChart();

    }




    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void showRestOfBudgetBarChart()
    {
        XYChart.Series dataSeries1 = new XYChart.Series();
       // dataSeries1.setName("2014");
/*
        for (Month month:monthObservableList)
        {
            System.out.println("in");
        }
*/

       /* for (int i=0;i<main.getMonthObservableList().size();i++)
        {
           // dataSeries1.getData().add(new XYChart.Data(main.getMonthObservableList().get(i).getName(),100));
            System.out.println("in");
        }*/
       System.out.println("ROZM Z MAIN:" +main.getMonthObservableList().size());


       for (int i =0;i<main.getMonthObservableList().size();i++)
       {
           dataSeries1.getData().add((new XYChart.Data(main.getMonthObservableList().get(i).getName()+" "+main.getMonthObservableList().get(i).getYear(),main.getMonthObservableList().get(i).getRestOfBudget())));
       }






        barChart.getData().add(dataSeries1);
    }

    public void setMain(Main main) {
        System.out.println("USTAWIENIE MAIN");
        this.main = main;
       // showRestOfBudgetBarChart();
    }

}
