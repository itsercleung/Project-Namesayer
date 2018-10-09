package namesayer;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import namesayer.util.HelpDialog;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.Name;
import namesayer.util.PlayAudio;
import org.controlsfx.control.Rating;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PractiseController implements Initializable {

    @FXML private ToggleSwitch toggleRandomise;
    @FXML private Button playNames;
    @FXML private Button practiseButton, uploadButton;
    @FXML private JFXTextField searchTextField;
    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private JFXListView<String> searchNamesView;
    @FXML private JFXListView<String> selectedNamesView;
    @FXML private Text userText, pointsText;

    private ObservableList<String> searchNameList = FXCollections.observableArrayList(); //List of all names
    private ObservableList<String> selectedNameList = FXCollections.observableArrayList();
    private static List<Name> namePlaylist = new ArrayList<>();
    private static List<PlayAudio> playAudioList = new ArrayList<>();
    private String concatName = "";
    private FilteredList<String> filteredData;
    private boolean isRandomised = false;

    private User user;

    @FXML
    private void exitPressed(ActionEvent event) {
        StackPane loginRoot = null;
        try {
            loginRoot = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(loginRoot);
    }

    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showHelpDialog(stackPane);
    }

    //Load testMicrophone pane
    @FXML
    private void testMicrophonePressed(ActionEvent event) {
        StackPane testMicrophoneRoot = null;
        try {
            testMicrophoneRoot = FXMLLoader.load(getClass().getResource("resources/TestMicrophone.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(testMicrophoneRoot);
    }

    //record new practise pane
    @FXML
    private void recordNamePressed(ActionEvent event) {
        StackPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/RecordNew.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    //Load play practise pane
    @FXML
    private void pressedPlayNames(ActionEvent event) {
        makePlayList(); //Create namePlayList according to selectedNames
        filterSelectedVideos();

        //Randomise toggle on/off randomises selected play list
        if (isRandomised) {
            Collections.shuffle(namePlaylist);
        }

        AnchorPane playRoot = null;
        try {
            playRoot = FXMLLoader.load(getClass().getResource("resources/Play.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(playRoot);
    }

    //Clear all to initial state
    @FXML
    private void clearButtonPressed(ActionEvent actionEvent) {
        namePlaylist.clear();
        selectedNameList.clear();
        toggleRandomise.setSelected(false);
    }

    //Load practise pane
    @FXML
    private void practisePressed(ActionEvent event) {
        StackPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/Practise.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    //User may have option to upload txt file for all names requested (as long as the name currently exists in system) and
    //meets line requires for each name.
    @FXML
    private void uploadButtonClicked(ActionEvent event) {
        // let the user select TXT files to upload
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        fc.setTitle("Select Text (.txt) File");
        fc.getExtensionFilters().add(filter);
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File textFile = fc.showOpenDialog(stage); // should be txt file

        //If user cancels, return gracefully
        if (textFile == null) {
            return;
        }
        Scanner reader = null;
        try {
            reader = new Scanner(textFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // go through text file and check if name is in names db
        while (reader.hasNextLine()) {
            String name = reader.nextLine().replace("-"," ");

            if (searchNameList.contains(name.toLowerCase())) {
                selectedNameList.add(name.toLowerCase());
            } else if (name.contains(" ") || name.contains("-")) {
                String[] names = name.split("[-\\s+]"); // whitespace delimiter with hyphen
                boolean canConcat = true;

                // go through list of names, if it is a name that exists in database, then add it
                for (String singleName : names) {
                    if (!searchNameList.contains(singleName.toLowerCase())) {
                        canConcat = false;
                        break;
                    }
                }
                if (canConcat) {
                    selectedNameList.add(name);
                }
            }
        }
        selectedNamesView.setItems(selectedNameList);
    }

    //Getting names of all name files in data/names
    private void populateList() {
        File namesFolder = new File("./data/names");
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
        double maxHeight = 180;
        double currentHeight = filteredData.size() * 30;
        if (currentHeight >= maxHeight) {
            searchNamesView.setMaxHeight(180);
        } else {
            searchNamesView.setMaxHeight(currentHeight);
        }
    }

    /* Makes playlist from selectedList names:
   (1) Use name to find all audio files associating with name
   (2) Get list of all audio files associating with name
   (3) Create a Name object for each element in list
   (4) Put into namePlayList to PLAY */
    private void makePlayList() {
        //Getting names of all name files in data/names
        File namesFolder = new File("./data/names");
        File[] listOfNames = namesFolder.listFiles();

        //(1) Get all name files with same name as selectedName
        for (String name : selectedNameList) {
            //Split names if combination
            if (name.contains("[COMBINE]: ")) {
                name = name.replace("[COMBINE]: ", "");
            }

            List<String> listOfSameName = new ArrayList<>(); //Duplicate names for each entry
            for (File file : listOfNames) {
                String fileName = file.getName();
                String[] parts = fileName.split("_");
                int extIndex = parts[3].indexOf(".");

                //If equal add to current List of same name
                if (name.equals(parts[3].substring(0, extIndex).toLowerCase())) {
                    listOfSameName.add(fileName);
                }
                //If name is combination split into parts to compare
                else if (name.contains(" ")) {
                    String[] names = name.split(" ");
                    for (String combName : names) {
                        if (combName.toLowerCase().equals(parts[3].substring(0, extIndex).toLowerCase())) {
                            listOfSameName.add(fileName);
                        }
                    }
                }
            }

            //(2) Look at existing ranks and pick the better ranks of names to play
            List<String> listOfHRNames = new ArrayList<>();
            try {
                byte[] bytes = Files.readAllBytes(Paths.get("./data/ratingAudio.txt"));
                String currentAudio = new String(bytes);
                if (!listOfSameName.isEmpty() && listOfSameName.size() > 1) {

                    //Go through current listOfSameName and find ranks from 3-5 or unranked (0) to filter
                    for (String currName : listOfSameName) {
                        if (currentAudio.contains(currName)) {
                            Scanner scanner = new Scanner(currentAudio);
                            while (scanner.hasNextLine()) {
                                String line = scanner.nextLine();
                                if (line.substring(0, line.length() - 4).equals(currName)) {
                                    double rating = Double.parseDouble(line.substring(line.length() - 3));
                                    //If 3,4,5 OR 0 then accept as high rank
                                    if (rating >= 3 || rating == 0) {
                                        listOfHRNames.add(currName);
                                    }
                                }
                            }
                        }
                        //Name audio isn't in rating system, thus treat as UNRANKED
                        else {
                            listOfHRNames.add(currName);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //(3a) If list has two different names (randomly select one for each name)
            String[] names = name.split(" ");
            if (names.length > 1) {
                List<List<String>> allHRNameList = new ArrayList<>(); //Stores lists of each name with high rank (HR) OR if no HR then add all
                List<String> chosenCombNames = new ArrayList<>(); //Randomly chosen list
                for (String combName : names) {

                    //Get all HR names associated with the name
                    List<String> HRNameList = new ArrayList<>();
                    for (String rankNames : listOfHRNames) {
                        if (combName.toLowerCase().equals(rankNames.substring(rankNames.lastIndexOf("_") + 1, rankNames.lastIndexOf(".")).toLowerCase())) {
                            HRNameList.add(rankNames);
                        }
                    }

                    //If name has no HR associated then get any of its duplicates
                    if (HRNameList.isEmpty()) {
                        int indexLR = 0;
                        for (String string : listOfSameName) {
                            if (string.toLowerCase().contains(combName.toLowerCase())) {
                                HRNameList.add(listOfSameName.get(indexLR));
                                break;
                            }
                            indexLR++;
                        }
                    }
                    allHRNameList.add(HRNameList);
                }

                //Combine the two names into one list (chosen randomly if a name has duplicates)
                for (List<String> list : allHRNameList) {
                    Random random = new Random();
                    String chosenName = list.get(random.nextInt(list.size()));
                    chosenCombNames.add(chosenName);
                }

                //Make Name object and add to selectionList (combination)
                String chosenName = chosenCombNames.get(0);
                String[] parts = chosenName.split("_");
                parts[3] = parts[3].substring(0,parts[3].lastIndexOf("."));
                for (int i = 1; i < chosenCombNames.size(); i++) {
                    chosenName = chosenCombNames.get(i);
                    String[] addParts = chosenName.split("_");
                    parts[0] = parts[0] + " " + addParts[0];
                    parts[1] = parts[1] + " " + addParts[1];
                    parts[2] = parts[2] + " " + addParts[2];
                    parts[3] = parts[3] + " " + addParts[3].substring(0,addParts[3].lastIndexOf("."));
                }
                namePlaylist.add(new Name(parts[3], parts[0], parts[1], parts[2], makeRating(0)));
            }
            //(3b) - (4) If listOfSameName is not empty or has an item, select a random file according to ranking
            else {
                if (!listOfHRNames.isEmpty()) {
                    Random random = new Random();
                    String chosenName = listOfHRNames.get(random.nextInt(listOfHRNames.size()));
                    String[] parts = chosenName.split("_");
                    int extIndex = parts[3].indexOf(".");
                    namePlaylist.add(new Name(parts[3].substring(0, extIndex), parts[0], parts[1], parts[2], makeRating(0))); //Make new Name object according to selected name file
                }
                //Else there is no high rank for following name, thus get any name in previous list
                else {
                    String chosenName = listOfSameName.get(0);
                    String[] parts = chosenName.split("_");
                    int extIndex = parts[3].indexOf(".");
                    namePlaylist.add(new Name(parts[3].substring(0, extIndex), parts[0], parts[1], parts[2], makeRating(0)));
                }
            }
        }

        //Sort namePlayList (can be randomized if user toggles)
        if (namePlaylist.size() > 0) {
            Collections.sort(namePlaylist, new Comparator<Name>() {
                @Override
                public int compare(final Name object1, final Name object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
        }
    }

    private void filterSelectedVideos() {
        for (Name playName : namePlaylist) {
            String nameAudio = playName.toString() + ".wav";

            //If current name isn't combination
            if (nameAudio.contains(" ")) {
                List<String> combineNameList = new ArrayList<>();
                String[] hypParts = nameAudio.split("_");
                String[] spcParts = hypParts[3].split(" ");
                int nameCount = spcParts.length;

                //Reformat and place into combineNameList
                for (int i = 0; i < nameCount; i++) {
                    StringBuilder name = new StringBuilder();
                    for (String part : hypParts) {
                        String[] spaceParts = part.split(" ");
                        name.append("_").append(spaceParts[i]);
                    }
                    if (name.toString().contains(".wav")) {
                        name = new StringBuilder(name.substring(0, name.lastIndexOf(".")));
                    }
                    combineNameList.add(name.substring(1));
                }

                //Run appropriate methods with given name list and play
                PlayAudio playAudio = new PlayAudio(combineNameList, nameAudio);
                playAudio.filterCombinedAudio();
                playAudioList.add(playAudio);
            }
        }
    }

    //Used in PlayController to get current selected Playlist of users
    public List<Name> getNamePlaylist() {
        return namePlaylist;
    }

    //Used to concat existing PlayAudio
    public List<PlayAudio> getPlayAudioList() {
        return playAudioList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Clear selectedLists and setup components
        namePlaylist.clear();
        selectedNameList.clear();
        playAudioList.clear();
        searchNamesView.setVisible(false);
        practiseButton.setDisable(true);

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

                    //Clear textfield in main thread
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            searchTextField.clear();
                            searchNameList.clear();
                            populateList();
                            mainRoot.requestFocus();
                        }
                    });
                }
            }
        });

        //Filtering and search function
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
                    concatName = String.join(" ", dispConcat).toLowerCase();
                    // get the filter list of names and add concat name to top of result list
                    List list = filteredData.getSource(); // TESTING: this might be a bad idea?
                    if (!list.contains("[COMBINE]: " + concatName)) {
                        list.add(0, "[COMBINE]: " + concatName);
                    }
                }
            }

            //Handle single word names through user search
            filteredData.setPredicate(name -> {
                // If filter text is empty, display all persons.
                if (newValue.equals("") || newValue.isEmpty()) {
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

                // Compare first name and last name of every client with filter text - if filter matches first name then RETURN
                changeHeightView();
                String lowerCaseFilter = newValue.toLowerCase();
                searchNamesView.setVisible(true);
                return name.toLowerCase().contains(lowerCaseFilter);
            });
        });

        //Adding search results to sortedList
        SortedList<String> sortedList = new SortedList<>(filteredData);
        searchNamesView.setItems(sortedList);

        // bind appropriate conditions to each button
        playNames.disableProperty().bind(Bindings.isEmpty(selectedNameList));
        toggleRandomise.disableProperty().bind(Bindings.size(selectedNameList).lessThan(2));

        // user details
        user = UserUtils.getCurrentLoginUser(userText,pointsText);
    }

}