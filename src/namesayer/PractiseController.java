package namesayer;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @FXML private AnchorPane mainRoot;
    @FXML private Button practiseButton;

    private ObservableList<Name> nameList = FXCollections.observableArrayList(); //List of all names
    private List<String> playList = new ArrayList<>(); //List of all names user selected
    private List<Name> namePlaylist = new ArrayList<>();
    private boolean isRandomised = false;
    private String currSelectedName; //Current name row selected by user

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

    private void populateListView() {
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

    //Update rating if user makes rate of specific audio row
    public void ratingPressed(String rating) {
        //Append the listed poorQualityAudio if the audio hasn't been rated already
        try {
            FileWriter writer = new FileWriter("data/ratingAudio.txt", true);
            byte[] bytes = Files.readAllBytes(Paths.get("data/ratingAudio.txt"));
            String currentAudio = new String(bytes);

            //Makes new line if audio has no existing rating otherwise overwrite rating
            if (!currentAudio.contains(currSelectedName)) {
                writer.write(currSelectedName + "-" + rating + "\n");
                writer.close();
            } else if (currentAudio.contains(currSelectedName)) {
                String newName = currSelectedName + "-" + rating;
                Scanner scanner = new Scanner(currentAudio);
                int lineCount = 0;

                //Creating list to append to specific line
                Path path = Paths.get("data/ratingAudio.txt");
                List<String> lines = Files.readAllLines(path);

                //Find line of audio and replace it with different rating
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.length() == newName.length()) {
                        if (line.substring(0, line.length()-3).equals(newName.substring(0, newName.length()-3))) {
                            lines.set(lineCount, newName);
                            Files.write(path, lines);
                        }
                    }
                    lineCount++;
                }
                scanner.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        practiseButton.setDisable(true);
        playNames.setDisable(true);

        populateListView(); //Populate and update lists
        ratingUpdate();

        //Assigns boolean value to isRandomised depending on toggleRandomise component
        toggleRandomise.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isRandomised = true; // on
            } else {
                isRandomised = false; // off
            }
        });

        //When user selects table row, get all column content
        TableView.TableViewSelectionModel<Name> selectionModel = nameTable.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((Observable observable) -> {
                /*//When user sets rating of specific name, update rating text file
                Rating rating = selectionModel.getSelectedItem().getRating();
                rating.ratingProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        ratingPressed(newValue.toString());
                    }
                });
                */
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

        nameTable.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
                        Node node = evt.getPickResult().getIntersectedNode();
            // go up from the target node until a row is found or it's clear the
            // target node wasn't a node.
            while (node != null && node != nameTable && !(node instanceof TableRow)) {
                node = node.getParent();
            }

            // handle event instead of using standard handling
            if (node instanceof TableRow) {
                // prevent further handling
                evt.consume();

                TableRow row = (TableRow) node;
                TableView tv = row.getTableView();

                // focus the tableview
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
    }
}