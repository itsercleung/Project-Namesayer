package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import namesayer.util.Name;
import org.controlsfx.control.Rating;
import org.controlsfx.control.ToggleSwitch;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PractiseController implements Initializable {

    @FXML private TableColumn<Name, String> nameCol;
    @FXML private TableColumn<Name, String> createdCol;
    @FXML private TableColumn<Name, String> dateCol;
    @FXML private TableColumn<Name, String> timeCol;
    @FXML private TableColumn<Name, Rating> ratingCol;
    @FXML private TableView<Name> nameTable;
    @FXML private ToggleSwitch toggleRandomise;
    @FXML private Button playNames;
    @FXML private Button practiseButton;
    @FXML private TextField searchTextField;
    @FXML private AnchorPane mainRoot;

    private ObservableList<Name> nameList = FXCollections.observableArrayList(); //List of all names
    private static List<Name> namePlaylist = new ArrayList<>(); //List of all names user selected [NAME]
    private static List<String> playList = new ArrayList<>(); //List of all names [String}
    private SortedList<Name> sortedNames;
    private boolean searchEmpty = true;
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
            Collections.shuffle(playList);
        }

        //Update playList with all selected names
        for (Name names : namePlaylist) {
            playList.add(names.toString());
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
        nameTable.getSelectionModel().clearSelection();
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

    public void populateTableView() {
        //Getting names of all name files in data/names
        File namesFolder = new File("data/names");
        File[] listOfNames = namesFolder.listFiles();

        for (File file : listOfNames) {
            String fileName = file.getName();
            String[] parts = fileName.split("_");
            int extIndex = parts[3].indexOf(".");

            //Set standard parts into each category
            Name nameObject = new Name(parts[3].substring(0,extIndex),parts[0],parts[1],parts[2],makeRating(0));
            nameList.add(nameObject);

            //Set onto table
            nameTable.setItems(nameList);
            nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
            createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("created"));
            dateCol.setCellValueFactory(new PropertyValueFactory<Name, String>("date"));
            timeCol.setCellValueFactory(new PropertyValueFactory<Name, String>("time"));
            ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));

            nameTable.getSortOrder().add(nameCol); //Sort by alphabetical names column
            nameTable.getSortOrder().add(createdCol);
        }
    }

    //Reselect names that have been selected by user
    private void selectUpdate() {
        int i = 0; //Index where selectedName is at on table
        for (Name sortedName : sortedNames) {
            for (Name selectedName : namePlaylist) {
                if (sortedName.toString().equals(selectedName.toString())) {
                    nameTable.getSelectionModel().select(i);
                }
            }
            i++;
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

    //Provides update to existing ratings to names that user has already done in the past
    public void ratingUpdate() {
        //If txt doesnt exist then make one and append TITLE
        File pqFile = new File("data/ratingAudio.txt");
        if (!pqFile.exists()) {
            try {
                pqFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            byte[] bytes = Files.readAllBytes(Paths.get("data/ratingAudio.txt"));
            String currentAudio = new String(bytes);

            //Once current audio is found in txt, extract its rating and update for audioRating component.
            Scanner scanner = new Scanner(currentAudio);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (Name name : nameList) {
                    if (line.substring(0, line.length() - 4).equals(name.toString())) {
                        double rating = Double.parseDouble(line.substring(line.length() - 3));
                        name.getRating().setRating(rating);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Name> getNamePlaylist() {
        return namePlaylist;
    }

    public List<String> getPlayList() {
        return playList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Clear selectedLists
        namePlaylist.clear();
        playList.clear();

        practiseButton.setDisable(true);
        playNames.setDisable(true);

        //Populate lists and ratings
        populateTableView();
        ratingUpdate();

        //Assigns boolean value to isRandomised depending on toggleRandomise component
        toggleRandomise.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isRandomised = true; // on
            } else {
                isRandomised = false; // off
            }
        });

        //Selecting multiple rows of tableView and setting to PracticeList
        nameTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        nameTable.setRowFactory(param -> {
            TableRow<Name> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Name name = row.getItem();
                    if (!namePlaylist.contains(name)) {
                        if (namePlaylist.isEmpty()) {
                            playNames.setDisable(false);
                        }
                        namePlaylist.add(name);
                    } else {
                        if (namePlaylist.size() == 1) {
                            playNames.setDisable(true);// last item being removed
                        }
                        namePlaylist.remove(name);
                    }
                }
            });
            return row;
        });

        //Select multiple names method - 2
        nameTable.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> { Node node = evt.getPickResult().getIntersectedNode();
            while (node != null && node != nameTable && !(node instanceof TableRow)) {
                node = node.getParent();
            }

            // handle event instead of using standard handling
            if (node instanceof TableRow) {
                // prevent further handling
                evt.consume();
                TableRow row = (TableRow) node;
                TableView tv = row.getTableView();
                tv.requestFocus();

                if (!row.isEmpty()) {
                    //If user deselects then name should be removed from list
                    int index = row.getIndex();
                    if (row.isSelected()) {
                        tv.getSelectionModel().clearSelection(index);
                    } else {
                        tv.getSelectionModel().select(index);
                    }
                }
            }
        });

        // attempt to auto filter list based on user input
        // put the name list in a filteredlist
        //TODO this filter removes all currently selected names
        FilteredList<Name> filteredList = new FilteredList<>(nameList, p -> true);
        // set filter predicate when filter changes
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(name -> {
                if (newValue == null || newValue.isEmpty()) {
                    selectUpdate();
                    return true; // display all names if empty textfield
                }
                else if (name.getName().contains(newValue)) {
                    selectUpdate();
                    return true; // textfield input is in list
                }
                selectUpdate();
                return false;
            });
        });

        // magic, puts filteredlist into a sortedlist and display on table
        sortedNames = new SortedList<>(filteredList);
        sortedNames.comparatorProperty().bind(nameTable.comparatorProperty());
        nameTable.setItems(sortedNames);
    }
}