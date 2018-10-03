package namesayer;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import namesayer.util.Name;
import namesayer.util.PlayAudio;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PlayController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private TableView<Name> nameTable;
    @FXML private TableColumn<Name, String> nameCol;
    @FXML private TableColumn<Name, String> createdCol;
    @FXML private TableColumn<Name, Rating> ratingCol;
    @FXML private Label playLabel;
    @FXML private Button prevButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private Button nextButton;
    @FXML private Button recordButton;
    @FXML private Rating audioRating;

    private PractiseController practiseController = new PractiseController();
    private int currentNameNum = 0; //Current name being played
    private ObservableList<Name> selectedList = FXCollections.observableArrayList(); //List of all selected names
    private String currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString(); //Current name row selected by user
    private PlayAudio playAudio;

    @FXML
    void exitPressed(ActionEvent event) {
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/Practise.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    @FXML
    void nextPressed(ActionEvent event) {
        //Switching to next selected audio files
        currentNameNum++;
        if (currentNameNum == practiseController.getNamePlaylist().size() - 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(false);
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);

        ratingUpdate(); //Update with current name rating
    }

    @FXML
    void prevPressed(ActionEvent event) {
        //Switching to prev selected audio files
        currentNameNum--;
        if (currentNameNum == 0) {
            prevButton.setDisable(true);
        }
        nextButton.setDisable(false);
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);

        ratingUpdate(); //Update with current name rating
    }

    @FXML
    void playPressed(ActionEvent event) {
        //Play audio files from filteredNames of the users selected
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        playButton.setDisable(true);
                        recordButton.setDisable(true);
                        stopButton.setDisable(false);
                        String path = "data/names/" + practiseController.getNamePlaylist().get(currentNameNum).toString();
                        playAudio = new PlayAudio(path);
                        playAudio.playAudio();
                    }
                });
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        playButton.setDisable(false);
                        recordButton.setDisable(false);
                        stopButton.setDisable(true);
                    }
                });
            }
        }.start();
    }

    @FXML
    void recordPressed(ActionEvent event) {

    }

    @FXML
    void stopPressed(ActionEvent event) {
        //If STOP pressed stop Audio method called and buttons reset
        playAudio.stopAudio();
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    //Update rating if user makes rate of specific audio row
    private void ratingPressed(String rating) {
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

    //Update editable rating component
    private void ratingUpdate() {
        //If txt doesnt exist then make one and append TITLE
        File pqFile = new File("data/ratingAudio.txt");
        if (!pqFile.exists()) {
            try {
                pqFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Read rating audio, and update names according to their existing ratings
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("data/ratingAudio.txt"));
            String currentAudio = new String(bytes);

            //Once current audio is found in txt, extract its rating and update for audioRating component.
            if (currentAudio.contains(practiseController.getNamePlaylist().get(currentNameNum).toString())) {
                Scanner scanner = new Scanner(currentAudio);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.substring(0, line.length() - 4).equals(practiseController.getNamePlaylist().get(currentNameNum).toString())) {
                        double rating = Double.parseDouble(line.substring(line.length() - 3));
                        selectedList.get(currentNameNum).getRating().setRating(rating); //Set column rating
                        audioRating.setRating(rating); //Set adjustable rating
                    }
                }
            }
            else {
                audioRating.setRating(0.0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateTableView() {
        //Create selectedList from playList in practiseController
        selectedList.addAll(practiseController.getNamePlaylist());

        //Set onto table using selectedList
        nameTable.setItems(selectedList);
        nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
        createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("created"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting table and rating updates and labels
        populateTableView();
        ratingUpdate();
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());

        //Accounting for single audio in which button is disabled
        if (practiseController.getNamePlaylist().size() == 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(true);
        stopButton.setDisable(true);
        nameTable.getSelectionModel().select(currentNameNum);
        currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString();

        //When user selects rating, update
        audioRating.ratingProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ratingPressed(newValue.toString());
                ratingUpdate();
            }
        });

        //Set icons to specific buttons from resources/icons (credited in description).
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playButton.setGraphic(new ImageView(play));
        Image stop = new Image(getClass().getResourceAsStream("resources/icons/stop.png"));
        stopButton.setGraphic(new ImageView(stop));
        Image prev = new Image(getClass().getResourceAsStream("resources/icons/back.png"));
        prevButton.setGraphic(new ImageView(prev));
        Image next = new Image(getClass().getResourceAsStream("resources/icons/next.png"));
        nextButton.setGraphic(new ImageView(next));
        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        recordButton.setGraphic(new ImageView(rec));
    }
}