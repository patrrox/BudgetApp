package info.ostaszewski.fxmlControllers;

import info.ostaszewski.Main;
import info.ostaszewski.domain.Month;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Asus on 15.09.2017.
 */
public class RootLayoutController {

    private Main main;

    private ObservableList<Month>monthObservableList= FXCollections.observableArrayList();


    public RootLayoutController(){

    }


    // give the reference
    public void setMain(Main main){
        this.main=main;
    }


    @FXML
    private void handleOpenStatsGeneral(){


       main.showGeneralStats();

    }

    @FXML
    private void handleClose(){
        main.exit();
    }


    @FXML
    private void initialize() {
    }



}
