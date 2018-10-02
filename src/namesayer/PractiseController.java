package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import namesayer.util.Name;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PractiseController implements Initializable {

    @FXML private ToggleSwitch toggleRandomise;
    @FXML private Button playNames;
    @FXML private Button practiseButton;
    @FXML private TextField searchTextField;
    @FXML private AnchorPane mainRoot;
    @FXML private ListView<String> searchNamesView;
    @FXML private ListView<String> selectedNamesView;

    private ObservableList<String> nameList = FXCollections.observableArrayList(); //List of all names
    private static List<Name> namePlaylist = new ArrayList<>(); //List of all names user selected
    private FilteredList<String> filteredData;
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
    private void pressedPlayNames(ActionEvent event) {
        //Randomise toggle on/off randomises selected play list
        if (isRandomised) {
            Collections.shuffle(namePlaylist);
        }

        //record new practise pane
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
        namePlaylist.clear();
        playNames.setDisable(true);
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

    private void populateList() {
        //Getting names of all name files in data/names
        File namesFolder = new File("data/names");
        File[] listOfNames = namesFolder.listFiles();

        for (File file : listOfNames) {
            String fileName = file.getName();
            String[] parts = fileName.split("_");
            int extIndex = parts[3].indexOf(".");

            //Check if duplicate (or already in list)
            boolean dupFlag = false;
            Name nameObject = new Name(parts[3].substring(0, extIndex));
            for (String stringName : nameList) {
                if (nameObject.getName().equalsIgnoreCase(stringName)) {
                    dupFlag = true;
                }
            }
            //If not duplicate add to list
            if (!dupFlag) {
                String upperName = nameObject.getName().toUpperCase(); //Consistency with cases
                nameList.add(upperName);
            }
        }
        Collections.sort(nameList);
    }

    private void changeHeightView() {
        double maxHeight = 165;
        double currentHeight = filteredData.size() * 23.5;
        if (currentHeight >= maxHeight) {
            searchNamesView.setMaxHeight(165);
        }
        else {
            searchNamesView.setMaxHeight(currentHeight);
        }
    }

    public List<Name> getNamePlaylist() {
        return namePlaylist;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Clear selectedLists
        namePlaylist.clear();
        searchNamesView.setVisible(false);

        practiseButton.setDisable(true);
        playNames.setDisable(true);

        //Populate and get information from directory
        populateList();

        //Assigns boolean value to isRandomised depending on toggleRandomise component
        toggleRandomise.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isRandomised = true; // on
            } else {
                isRandomised = false; // off
            }
        });

        //TODO: Handle user clicking something from [searchNamesView] and updating [selectNamesView] (if clicked on name, add name to selectedNamesView, and clear search and view)

        //Filtering and search function
        //TODO: Able to handle user input of ("-" and " ") characters. Maybe do : (if all names exist in database, show the combination on searchNamesView)
        //TODO: I guess if searchNamesView is empty from users search input, then do the process of seeing if the combination exists in database (could possibly add a method to make this easier).
        filteredData = new FilteredList<>(nameList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(name ->{
                // If filter text is empty, display all persons.
                if (newValue.equals("") || newValue.isEmpty()){
                    searchNamesView.setVisible(false);
                    return true;
                }

                // Compare first name and last name of every client with filter text.
                // and if filter matches first name then RETURN
                changeHeightView();
                String lowerCaseFilter = newValue.toLowerCase();
                searchNamesView.setVisible(true);
                return name.toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<String> sortedList = new SortedList<>(filteredData); //Using sortedList to handle display view
        searchNamesView.setItems(sortedList);
    }
}