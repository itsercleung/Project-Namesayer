package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import namesayer.util.CreateTempAudio;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private ObservableList<Name> selectedList = FXCollections.observableArrayList(); //List of all selected names
    private int currentNameNum = 0; //Current name being played
    private String currSelectedName; //Current name row selected by user
    private PlayAudio playAudio;

    private JFXPopup recordPopup = new JFXPopup();
    private JFXButton playOldButton = new JFXButton("PLAY OLD"); // placeholders, can put way better looking buttons
    private JFXButton playNewButton = new JFXButton("PLAY NEW");
    private JFXButton recordSubButton = new JFXButton("RECORD");
    private JFXButton saveButton = new JFXButton("SAVE NEW");
    private String tempAudioName = null; // for recording

    @FXML
    void exitPressed(ActionEvent event) {
        //Delete temp combined names if they exist
        MainController mainController = new MainController();
        mainController.deleteTemp();

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
        updateRatingComponent(); //Change rating component
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
        updateRatingComponent(); //Change rating component
    }


    @FXML
    void rowClicked(MouseEvent event) {
        //Set selectedName index on what row user selects
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        int i = 0;
        for (Name currName : selectedList) {
            if (currName.toString().equals(currSelectedName)) {
                currentNameNum = i;
            }
            i++;
        }

        //Disable buttons if they're boundaries
        prevButton.setDisable(false);
        nextButton.setDisable(false);
        if (currentNameNum == 0) {
            prevButton.setDisable(true);
        }
        else if (currentNameNum == practiseController.getNamePlaylist().size() - 1) {
            nextButton.setDisable(true);
        }
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        updateRatingComponent(); //Change rating component
    }

    @FXML
    void playPressed(ActionEvent event) {
        playMethod();
    }

    // for code reuse
    private void playMethod() {
        //Play audio files from filteredNames of the users selected
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        //Set appropriate button layout
                        playButton.setDisable(true);
                        recordButton.setDisable(true);
                        stopButton.setDisable(false);

                        String nameAudio = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";
                        //If current name isn't combination
                        if (!nameAudio.contains(" ")) {
                            playAudio = new PlayAudio("data/names/" + nameAudio);
                            playAudio.playAudio();

                        }
                        //Else if name is combination - section names into appropriate format
                        else {
                            playAudio = new PlayAudio("temp/" + nameAudio.replace(" ", ""));
                            playAudio.playAudio();
                        }
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
        // TODO to recordSubButton user practise
        // TODO also a play/save should be in same screen?
        // TODO: Possibly let user toggle popup (so if they click out of it, it should stay there) We could make a menu out of that maybe.
        // TODO: What do you think??

        initRecordPopup();
        recordPopup.show(recordButton,JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT);

    }

    private void initRecordPopup() {
        // this method will prepare the buttons within the popup
        VBox box = new VBox(recordSubButton, playOldButton, playNewButton, saveButton); // can make HBox
        recordPopup.setPopupContent(box);

    }

    @FXML
    void stopPressed(ActionEvent event) {
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
            currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

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
                        if (line.substring(0, line.length() - 3).equals(newName.substring(0, newName.length() - 3))) {
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
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("data/ratingAudio.txt"));
            String currentAudio = new String(bytes);

            //Once current audio is found in txt, extract its rating and update for audioRating component.
            for (int i = 0; i < selectedList.size(); i++) {
                if (currentAudio.contains(practiseController.getNamePlaylist().get(i).toString() + ".wav")) {
                    Scanner scanner = new Scanner(currentAudio);
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.substring(0, line.length() - 4).equals(practiseController.getNamePlaylist().get(i).toString() + ".wav")) {
                            double rating = Double.parseDouble(line.substring(line.length() - 3));
                            selectedList.get(i).getRating().setRating(rating); //Set column rating
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Update rating change on rateable component
    private void updateRatingComponent() {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get("data/ratingAudio.txt"));
            String currentAudio = new String(bytes);
            String currentPlay = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

            if (currentAudio.contains(currentPlay)) {
                Scanner scanner = new Scanner(currentAudio);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.substring(0, line.length() - 4).equals(currentPlay)) {
                        double rating = Double.parseDouble(line.substring(line.length() - 3));
                        audioRating.setRating(rating); //Set adjustable rating
                    }
                }
            } else {
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
        createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("createdDesc"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting table and rating updates
        populateTableView();
        ratingUpdate();
        updateRatingComponent();
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());

        //Accounting for single audio in which button is disabled
        if (practiseController.getNamePlaylist().size() == 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(true);
        stopButton.setDisable(true);
        nameTable.getSelectionModel().select(currentNameNum);
        currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

        //When user selects rating, update
        audioRating.ratingProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ratingPressed(newValue.toString());
                ratingUpdate();
            }
        });

        // logic that deals with the JFXPopup for record button
        recordButton.setDisable(true);
        saveButton.setDisable(true);
        playOldButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playMethod();
            }
        });

        playNewButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String path = "./temp/" +tempAudioName + ".wav";
                PlayAudio playAudio = new PlayAudio(path);
                playAudio.playAudio();
            }
        });

        recordSubButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                playNewButton.setDisable(false);
                saveButton.setDisable(false);

                //Setup official name for saved recording
                DateFormat dateFormat = new SimpleDateFormat("_dd-MM-yyyy_HH-mm-ss_");
                Date date = new Date();
                tempAudioName = "user" + dateFormat.format(date) +
                        practiseController.getNamePlaylist().get(currentNameNum).getName();

                CreateTempAudio cta = new CreateTempAudio(tempAudioName);
                cta.createSingleAudio();
            }
        });

        //Concat multiple names selected
        for (PlayAudio audio : practiseController.getPlayAudioList()) {
            audio.concatCombinedAudio();
        }

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