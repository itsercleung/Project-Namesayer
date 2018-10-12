package namesayer;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import namesayer.util.*;
import namesayer.login.User;
import namesayer.login.UserUtils;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PractiseController implements Initializable {

    @FXML private ToggleSwitch toggleRandomise;
    @FXML private Button playNames;
    @FXML private Button practiseButton, uploadButton;
    @FXML private Button helpButton, rewardButton, exitButton;
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

    private FXMLResourceLoader loader = new FXMLResourceLoader();

    @FXML private void exitPressed(ActionEvent event) {
        StackPane loginRoot = null;
        loader.load(FXMLResource.LOGOUT, loginRoot, mainRoot);
    }

    //Load help popup
    @FXML private void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    //Load rewards window
    @FXML private void rewardPressed(ActionEvent event) {
        StackPane rewardsRoot = null;
        loader.load(FXMLResource.REWARD, rewardsRoot, mainRoot);
    }

    @FXML private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        StackPane testMicrophoneRoot = null;
        loader.load(FXMLResource.TEST_MICROPHONE,testMicrophoneRoot,mainRoot);
    }

    @FXML private void practisePressed(ActionEvent event) {
        //Load practise pane
        StackPane practiseRoot = null;
        loader.load(FXMLResource.PRACTISE,practiseRoot,mainRoot);
    }

    @FXML private void recordNamePressed(ActionEvent event) {
        //record new practise pane
        StackPane practiseRoot = null;
        loader.load(FXMLResource.RECORD_NEW,practiseRoot,mainRoot);
    }

    //Load play practise pane
    @FXML
    private void pressedPlayNames(ActionEvent event) {
        //Call on PlayListCreator class to create the playList and filter such names
        PlayListCreator playListCreator = new PlayListCreator(selectedNameList);
        playListCreator.makePlayList();
        playListCreator.filterSelectedVideos(); //Filter (concat/non-concat)
        namePlaylist = playListCreator.getNamePlaylist();
        playAudioList = playListCreator.getPlayAudioList();

        //Randomise toggle on/off randomises selected play list
        if (isRandomised) {
            Collections.shuffle(namePlaylist);
        }

        AnchorPane playRoot = null;
        loader.load(FXMLResource.PLAY,playRoot,mainRoot);
    }

    //Clear all to initial state
    @FXML
    private void clearButtonPressed(ActionEvent actionEvent) {
        namePlaylist.clear();
        selectedNameList.clear();
        toggleRandomise.setSelected(false);
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

        List<String> rejectList = new ArrayList<>();
        // go through text file and check if name is in names db
        while (reader.hasNextLine()) {
            String name = reader.nextLine().replace("-"," ").toLowerCase();

            if (!selectedNameList.contains(name)) {
                if (searchNameList.contains(name)) {
                    selectedNameList.add(name);
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
                        selectedNameList.add("[COMBINE]: " + name);
                    }
                } else {
                    // add name to reject list
                    rejectList.add(name);
                }
            }
            else if (selectedNameList.contains("[COMBINE]: " + name)) {
                duplicateCheck(name);
            }
        }

        if (rejectList.size() > 0) {
            String label = "The following names are not available.\n" +
                    rejectList.toString() +"\n"+// temporary proof of concept
                    "Be the first to create one of these names!";
            Button button = new Button("Got it");
            button.getStylesheets().add("namesayer/resources/stylesheet/general.css");
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(stackPane,layout, JFXDialog.DialogTransition.TOP);
            layout.setHeading(new Label("Note"));
            layout.setBody(new Label(label));
            layout.setActions(button);
            dialog.show();

            //Set action on to close
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });
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

    private void duplicateCheck(String name) {
        //Show JFX dialog to warn user about duplicate play name
        if (selectedNameList.contains(name)) {
            Button button = new Button("Got it");
            button.getStylesheets().add("namesayer/resources/stylesheet/general.css");
            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
            dialogLayout.setHeading(new Label("Note"));
            dialogLayout.setBody(new Label("Sorry! " + name + " already in your play list!"));
            dialogLayout.setActions(button);
            dialog.show();

            //Set action on to close
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
                dialog.close();
            });

            searchNamesView.setVisible(false);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    searchTextField.clear();
                }
            });
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
                String selectedName = searchNamesView.getSelectionModel().getSelectedItem();
                if (selectedName != null) {
                    if (!selectedNameList.contains(selectedName)) {
                        duplicateCheck(selectedName);
                        selectedNameList.add(selectedName);
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
                    else if (selectedNameList.contains(selectedName)) {
                        duplicateCheck(selectedName);
                    }
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

        // Reward and help Popup icons
        Image reward = new Image(getClass().getResourceAsStream("resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));
        Image help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));
        helpButton.setGraphic(new ImageView(help));
        Image logout = new Image(getClass().getResourceAsStream("resources/icons/sign-out.png"));
        exitButton.setGraphic(new ImageView(logout));
    }

}