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
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.*;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PlayController implements Initializable {

    @FXML
    private AnchorPane mainRoot;
    @FXML
    private TableView<Name> nameTable;
    @FXML
    private TableColumn<Name, String> nameCol;
    @FXML
    private TableColumn<Name, String> createdCol;
    @FXML
    private TableColumn<Name, Rating> ratingCol;
    @FXML
    private Label playLabel;
    @FXML
    private Text userText, pointsText;
    @FXML
    private Button nextButton, recordButton, stopButton, playButton, prevButton;
    @FXML
    private Button rewardButton, exitButton;
    @FXML
    private Rating audioRating;

    private PractiseController practiseController = new PractiseController();
    private ObservableList<Name> selectedList = FXCollections.observableArrayList(); //List of all selected names
    private int currentNameNum = 0; //Current name being played
    private String currSelectedName; //Current name row selected by user
    private PlayAudio playAudio;
    private RatingManager ratingManager; //Rating instance for rating changes and updates
    private User user;

    //Record sidebar function
    private JFXPopup recordPopup = new JFXPopup();
    private JFXButton playOldButton = new JFXButton(" PLAY OLD");
    private JFXButton playOldThreeButton = new JFXButton("PLAY OLD X3");
    private JFXButton playNewButton = new JFXButton("PLAY NEW");
    private JFXButton recordSubButton = new JFXButton("RECORD");
    private JFXButton saveButton = new JFXButton("SAVE NEW");
    private String tempAudioName = null; // for recording
    private CreateAudio createAudio;

    private FXMLResourceLoader loader = new FXMLResourceLoader();
    private PlayManager playManager = new PlayManager(playButton,recordButton,stopButton);

    @FXML
    void exitPressed(ActionEvent event) {
        //Delete temp combined names if they exist
        MainController mainController = new MainController();
        mainController.deleteTemp();

        StackPane practiseRoot = null;
        loader.load(FXMLResource.PRACTISE, practiseRoot, mainRoot);
    }

    //Load rewards window
    @FXML
    private void rewardPressed(ActionEvent event) {
        StackPane rewardsRoot = null;
        loader.load(FXMLResource.REWARD, rewardsRoot, mainRoot);
    }

    //Switches to next name audio if there exists next audio
    @FXML
    void nextPressed(ActionEvent event) {
        //Switching to next selected audio files
        currentNameNum++;
        if (currentNameNum == practiseController.getNamePlaylist().size() - 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(false);

        //Update rating and components
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);
        ratingManager.updateRatingComponent(currentNameNum);
    }

    //Switches back to previous name audio if there exists previous audio
    @FXML
    void prevPressed(ActionEvent event) {
        //Switching to prev selected audio files
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        currentNameNum--;
        if (currentNameNum == 0) {
            prevButton.setDisable(true);
        }
        nextButton.setDisable(false);
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);
        ratingManager.updateRatingComponent(currentNameNum);
    }

    //Changes current audio to whatever user selects
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
        if (currentNameNum == practiseController.getNamePlaylist().size() - 1) {
            nextButton.setDisable(true);
        }
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        ratingManager.updateRatingComponent(currentNameNum);
    }

    //Plays current selected name audio for 5 seconds
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
                            playAudio = new PlayAudio("./data/names/" + nameAudio);
                            playAudio.playAudio();
                        }
                        //Else if name is combination - section names into appropriate format
                        else {
                            playAudio = new PlayAudio("./temp/" + nameAudio.replace(" ", ""));
                            playAudio.playAudio();
                        }
                    }
                });
                try {
                    Thread.sleep(4000);
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

    //Creates a popup menu with record,compare plays, and save buttons for user to navigate
    @FXML
    void recordPressed(ActionEvent event) {
        //Button sizes and style
        recordSubButton.setMinSize(130.0, 40);
        playNewButton.setMinSize(130.0, 40);
        playOldButton.setMinSize(130.0, 40);
        saveButton.setMinSize(130.0, 40);

        //Setting popup position and size
        recordPopup.show(recordButton, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 80, 0);
        recordPopup.getPopupContent().setPrefWidth(130);
        recordPopup.setStyle("-fx-effect: null;" + "-fx-drop-shadow: null;");
    }

    //Stop currently playing name (in case user doesn't want to wait 5 seconds)
    @FXML
    void stopPressed(ActionEvent event) {
        playAudio.stopAudio();
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    //Populates users selected Play List in the Practice Controller - then forms table with appropriate details
    private void populateTableView() {
        //Create selectedList from playList in practiseController
        selectedList.addAll(practiseController.getNamePlaylist());

        //Set onto table using selectedList
        nameTable.setItems(selectedList);
        nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
        createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("createdDesc"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));
    }

    private void playNew() {
        String path = "./temp/" + tempAudioName.replace(" ", "") + ".wav";
        PlayAudio playAudio = new PlayAudio(path);
        playAudio.playAudio();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting table and rating updates using RatingManager class
        populateTableView();
        ratingManager = new RatingManager(selectedList, practiseController, audioRating);
        selectedList = ratingManager.ratingUpdate();
        ratingManager.updateRatingComponent(currentNameNum);
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());

        //Accounting for single audio in which button is disabled
        if (practiseController.getNamePlaylist().size() == 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(true);
        stopButton.setDisable(true);
        nameTable.getSelectionModel().select(currentNameNum);
        currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

        //Concat multiple names selected
        for (PlayAudio audio : practiseController.getPlayAudioList()) {
            audio.concatCombinedAudio();
        }

        //When user selects rating, update
        audioRating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            currSelectedName = ratingManager.ratingPressed(newValue.toString(), currentNameNum);
            selectedList = ratingManager.ratingUpdate();
        });

        //JFXPOPUP BUTTON ACTIONS
        //PLAYOLD - plays current names audio file
        saveButton.setDisable(true);
        playOldButton.setOnMousePressed(event -> playMethod());

        //PLAYNEW - plays users recorded version of name file (if exists)
        playNewButton.setDisable(true);
        playNewButton.setOnMousePressed(event -> {
            playNew();
        });

        //PLAYOLDNEWX3
        playOldThreeButton.setOnMousePressed(event -> {
            // TODO alternate between old and new
            // can't get past threading issues
            new Thread(() ->
            {
                Platform.runLater( ()-> {
                    //Set appropriate button layout
                    playButton.setDisable(true);
                    recordButton.setDisable(true);
                    stopButton.setDisable(false);

                    String nameAudio = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";
                    //If current name isn't combination
                    if (!nameAudio.contains(" ")) {
                        playAudio = new PlayAudio("./data/names/" + nameAudio);
                        playAudio.playAudio();
                    }
                    //Else if name is combination - section names into appropriate format
                    else {
                        playAudio = new PlayAudio("./temp/" + nameAudio.replace(" ", ""));
                        playAudio.playAudio();
                    }
                });
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(this::playNew);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        //RECORD - records user trying to pronounce PLAYOLD name
        recordSubButton.setOnMousePressed(event -> {
            playNewButton.setDisable(false);
            saveButton.setDisable(false);

            //Setup official name for saved recording
            new Thread() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            //Disable buttons
                            playNewButton.setDisable(true);
                            playOldButton.setDisable(true);
                            recordSubButton.setDisable(true);
                            saveButton.setDisable(true);
                            playButton.setDisable(true);

                            tempAudioName = "user_" + practiseController.getNamePlaylist().get(currentNameNum).replaceDesc();
                            createAudio = new CreateAudio(tempAudioName);
                            createAudio.createSingleAudio();
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        public void run() {
                            //Reenable buttons
                            playNewButton.setDisable(false);
                            playOldButton.setDisable(false);
                            recordSubButton.setDisable(false);
                            saveButton.setDisable(false);
                            playButton.setDisable(false);

                            //Disable rating and save buttons for concat
                            if (currSelectedName.contains(" ")) {
                                saveButton.setDisable(true);
                            }
                        }
                    });
                }
            }.start();
        });

        //SAVE NEW - user attempt placed into database to add onto filtering
        saveButton.setOnMousePressed(event -> createAudio.saveAudio());

        //INIT JFX-POPUP
        VBox box = new VBox(recordSubButton, playOldButton, playOldThreeButton, playNewButton, saveButton); // can make HBox
        recordPopup.setPopupContent(box);

        //Set user current name and score
        user = UserUtils.getCurrentLoginUser(userText, pointsText);

        //Set icons to specific buttons from resources/icons (credited in description).
        //Set icons for play menu
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playButton.setGraphic(new ImageView(play));
        Image stop = new Image(getClass().getResourceAsStream("resources/icons/stop.png"));
        stopButton.setGraphic(new ImageView(stop));
        Image prev = new Image(getClass().getResourceAsStream("resources/icons/back.png"));
        prevButton.setGraphic(new ImageView(prev));
        Image next = new Image(getClass().getResourceAsStream("resources/icons/next.png"));
        nextButton.setGraphic(new ImageView(next));

        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordButton.setGraphic(new ImageView(rec));
        recordButton.setOnMouseEntered(e -> recordButton.setGraphic(new ImageView(recHover)));
        recordButton.setOnMouseExited(e -> recordButton.setGraphic(new ImageView(rec)));

        //Set icons for side menu
        Image recSub = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recSubHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordSubButton.setGraphic(new ImageView(recSub));
        Image playOld = new Image(getClass().getResourceAsStream("resources/icons/playOld.png"));
        Image playOldHover = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playOldButton.setGraphic(new ImageView(playOld));
        Image playNew = new Image(getClass().getResourceAsStream("resources/icons/playNew.png"));
        Image playNewHover = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playNewButton.setGraphic(new ImageView(playNew));
        Image saveNew = new Image(getClass().getResourceAsStream("resources/icons/save.png"));
        Image saveNewHover = new Image(getClass().getResourceAsStream("resources/icons/saveHover.png"));
        saveButton.setGraphic(new ImageView(saveNew));
        Image logout = new Image(getClass().getResourceAsStream("resources/icons/sign-out.png"));
        exitButton.setGraphic(new ImageView(logout));

        // Reward Popup icons
        Image reward = new Image(getClass().getResourceAsStream("resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));

        //Setting styles for side menu bar buttons and EVENTS on hover
        recordSubButton.setFocusTraversable(false);
        playNewButton.setFocusTraversable(false);
        playOldButton.setFocusTraversable(false);
        saveButton.setFocusTraversable(false);

        //Setting mouse hover events
        recordSubButton.setOnMouseEntered(e -> {
            recordSubButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            recordSubButton.setGraphic(new ImageView(recSubHover));
        });
        recordSubButton.setOnMouseExited(e -> {
            recordSubButton.setStyle("-fx-background-color: transparent");
            recordSubButton.setGraphic(new ImageView(recSub));
        });

        playNewButton.setOnMouseEntered(e -> {
            playNewButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            playNewButton.setGraphic(new ImageView(playNewHover));
        });

        playNewButton.setOnMouseExited(e -> {
            playNewButton.setStyle("-fx-background-color: transparent");
            playNewButton.setGraphic(new ImageView(playNew));
        });

        playOldButton.setOnMouseEntered(e -> {
            playOldButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            playOldButton.setGraphic(new ImageView(playOldHover));
        });

        playOldButton.setOnMouseExited(e -> {
            playOldButton.setStyle("-fx-background-color: transparent");
            playOldButton.setGraphic(new ImageView(playOld));
        });

        saveButton.setOnMouseEntered(e -> {
            saveButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            saveButton.setGraphic(new ImageView(saveNewHover));
        });

        saveButton.setOnMouseExited(e -> {
            saveButton.setStyle("-fx-background-color: transparent");
            saveButton.setGraphic(new ImageView(saveNew));
        });
    }
}