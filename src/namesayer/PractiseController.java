package namesayer;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import namesayer.util.Name;
import org.controlsfx.control.Rating;
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

    private ObservableList<String> searchNameList = FXCollections.observableArrayList(); //List of all names
    private ObservableList<String> selectedNameList = FXCollections.observableArrayList();
    private static List<Name> namePlaylist = new ArrayList<>();
    private String concatName = "";
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
        //Create namePlayList according to selectedNames
        makePlayList();

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
        selectedNameList.clear();
        playNames.setDisable(true);
        System.out.println(concatName);
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
            for (String stringName : searchNameList) {
                if (nameObject.getName().equalsIgnoreCase(stringName)) {
                    dupFlag = true;
                }
            }
            //If not duplicate add to list
            if (!dupFlag) {
                String upperName = nameObject.getName().toLowerCase(); //Consistency with cases
                searchNameList.add(upperName);
            }
        }
        Collections.sort(searchNameList);
    }

    private void makePlayList() {
        //Getting names of all name files in data/names
        File namesFolder = new File("data/names");
        File[] listOfNames = namesFolder.listFiles();

        //Get all name files with same name as selectedName
        for (String name : selectedNameList) {
            List<String> listOfSameName = new ArrayList<>();
            for (File file : listOfNames) {
                String fileName = file.getName();
                String[] parts = fileName.split("_");
                int extIndex = parts[3].indexOf(".");

                //If equal add to current List of same name
                if (name.equals(parts[3].substring(0, extIndex).toLowerCase())) {
                    listOfSameName.add(fileName);
                }
            }
            System.out.println(listOfSameName);
            //If listOfSameName is not empty or has more than 1 item, select a random file according to ranking
            if (!listOfSameName.isEmpty() && listOfSameName.size() > 1) {
                Random randomiser = new Random();
                String chosenName = listOfSameName.get(randomiser.nextInt(listOfSameName.size()));
                String[] parts = chosenName.split("_");
                int extIndex = parts[3].indexOf(".");
                namePlaylist.add(new Name(parts[3].substring(0, extIndex),parts[0], parts[1], parts[2], makeRating(0))); //Make new Name object according to selected name file
            }
            else if (!listOfSameName.isEmpty()) {
                String chosenName = listOfSameName.get(0);
                String[] parts = chosenName.split("_");
                int extIndex = parts[3].indexOf(".");
                namePlaylist.add(new Name(parts[3].substring(0, extIndex),parts[0], parts[1], parts[2], makeRating(0))); //Make new Name object according to selected name file
            }
            System.out.println(namePlaylist);
        }
    }

    //When user sets rating on a name then makeRating set
    private Rating makeRating(double rate) {
        Rating rating = new Rating();
        rating.setOrientation(Orientation.HORIZONTAL);
        rating.setUpdateOnHover(false);
        rating.setPartialRating(false);
        rating.setRating(rate);
        rating.setDisable(true);
        return rating;
    }

    //Dynamically changes height of the listView (scales up to be similar to a search drop down)
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
        //Clear selectedLists and setup components
        namePlaylist.clear();
        selectedNameList.clear();
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

        //Add selected name from search onto selectedNamesList and update such view
        searchNamesView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (searchNamesView.getSelectionModel().getSelectedItem() != null) {
                    selectedNameList.add(searchNamesView.getSelectionModel().getSelectedItem());
                    selectedNamesView.setItems(selectedNameList);
                    searchNamesView.setVisible(false);
                    playNames.setDisable(false); //Enables play button once a name is selected

                    //Clear textfield in main thread
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            searchTextField.clear();
                            mainRoot.requestFocus();
                        }
                    });
                }
            }
        });

        //Filtering and search function
        //TODO: Able to handle user input of ("-" and " ") characters. Maybe do : (if all names exist in database, show the combination on searchNamesView)
        //TODO: I guess if searchNamesView is empty from users search input, then do the process of seeing if the combination exists in database (could possibly add a method to make this easier).
        filteredData = new FilteredList<>(searchNameList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {

            //IF "-" or " " exists from user input and such words exist in database, get the concatenated version and display on search
            String[] dispConcat;
            int wordCount = 0;
            if (newValue.contains("-") || newValue.contains(" ")) {
                dispConcat = newValue.split("[-\\s+]"); // whitespace delimiter with hyphen
                for (String singleName : dispConcat) {
                    for (String names : searchNameList) {
                        if (names.toLowerCase().equals(singleName.toLowerCase())) {
                            wordCount++;
                        }
                    }
                }

                if (wordCount == dispConcat.length && wordCount > 1) {
                    concatName = String.join(" ", dispConcat);
                    System.out.println(concatName);
                }
            }

            //Handle single word names through user search
            filteredData.setPredicate(name ->{
                // If filter text is empty, display all persons.
                if (newValue.equals("") || newValue.isEmpty()){
                    searchNamesView.setVisible(false);
                    return true;
                }

                // splitting string by "-" and/or " "
                String[] allNames;
                if (newValue.contains("-") || newValue.contains(" ")) {
                    allNames = newValue.split("[-\\s+]"); // whitespace delimiter with hyphen
                    for (String singleName : allNames) {
                        if (name.toLowerCase().contains(singleName.toLowerCase())) {
                            return true;
                        }
                    }
                }

                // Compare first name and last name of every client with filter text.
                // and if filter matches first name then RETURN
                changeHeightView();
                String lowerCaseFilter = newValue.toLowerCase();
                searchNamesView.setVisible(true);
                return name.toLowerCase().contains(lowerCaseFilter);
            });
        });

        //Adding search results to sortedList
        SortedList<String> sortedList = new SortedList<>(filteredData);
        searchNamesView.setItems(sortedList);
    }
}