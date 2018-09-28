package namesayer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import namesayer.util.MultipleListener;
import namesayer.util.Name;
import namesayer.util.UpdateName;
import org.controlsfx.control.CheckListView;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class PractiseController implements Initializable {

    @FXML private TableColumn<Name, String> nameCol;
    @FXML private TableColumn<Name, String> createdCol;
    @FXML private TableColumn<Name, String> dateCol;
    @FXML private TableColumn<Name, String> timeCol;
    @FXML private TableColumn<?, ?> ratingCol;
    @FXML private TableView<Name> nameTable;
    @FXML private Button clearButton;
    @FXML private ToggleSwitch toggleRandomise;
    @FXML private Button playNames;
    @FXML private AnchorPane mainRoot;
    @FXML private Button practiseButton;

    private ObservableList<Name> nameList = FXCollections.observableArrayList();
    private List<String> playList = new ArrayList<>();
    private boolean isRandomised = false;

    @FXML
    private void exitPressed(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        AnchorPane testMicrophoneRoot = null;
        try {
            testMicrophoneRoot = FXMLLoader.load(getClass().getResource("resources/TestMicrophone.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(testMicrophoneRoot);
    }

    @FXML
    private void recordNamePressed(ActionEvent event) {
        //record new practise pane
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/RecordNew.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    @FXML
    private void pressedPlayNames(ActionEvent actionEvent) {
        //Deciding if randomisation is toggled on/off to shuffle playList of selected items
        if (isRandomised) {
            Collections.shuffle(playList);
        }
        //Output List<String> into audioList.txt
        try {
            FileWriter writer = new FileWriter("temp/audioList.txt");
            for(String str: playList) {
                writer.write(str+"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Load Play pane
        AnchorPane playRoot = null;
        try {
            playRoot = FXMLLoader.load(getClass().getResource("resources/Play.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(playRoot);
    }

    @FXML
    private void clearButtonPressed(ActionEvent actionEvent) {

    }

    private void populateListView() {
        //Getting names of all name files in data/names
        File namesFolder = new File("data/names");
        File[] listOfNames = namesFolder.listFiles();

        for (File file : listOfNames) {
            String fileName = file.getName();
            String[] parts = fileName.split("_");
            int extIndex = parts[3].indexOf(".");

            //Set standard parts into each category
            Name nameObject = new Name(parts[3].substring(0,extIndex),parts[0],parts[1],parts[2]);
            nameList.add(nameObject);

            //Set onto table
            nameTable.setItems(nameList);
            nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
            createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("created"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Name, String>("date"));
            timeCol.setCellValueFactory(new PropertyValueFactory<Name, String>("time"));
        }

    }

    @FXML
    private void practisePressed(ActionEvent event) {
        //Load practise pane
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/Practise.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiseButton.setDisable(true);
        playNames.setDisable(true);

        populateListView(); //Populate and update lists

        //Assigns boolean value to isRandomised depending on toggleRandomise component
        toggleRandomise.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isRandomised = true; // on
            } else {
                isRandomised = false; // off
            }
        });
    }
}