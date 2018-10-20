package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import namesayer.login.Points;
import namesayer.login.UserUtils;
import namesayer.util.*;
import org.controlsfx.control.Rating;

/**
 * PlayController : Deals with users selected playList to practise names in. Using such playList it updates an existing
 * table with its specified name, creator and rating. It provides play components, recording components and dynamic name
 * changing components.
 */
public class PlayController extends NameSayerMenuController implements Initializable {

    @FXML private TableView<Name> nameTable;
    @FXML private TableColumn<Name, String> nameCol, createdCol;
    @FXML private TableColumn<Name, Rating> ratingCol;
    @FXML private Label playLabel;
    @FXML private Button nextButton, recordButton, stopButton,
            playButton, prevButton;
    @FXML private Rating audioRating;
    @FXML private StackPane stackPane;

    private PractiseController practiseController = new PractiseController();
    private ObservableList<Name> selectedList = FXCollections.observableArrayList(); //List of all selected names
    private int currentNameNum = 0; //Current name being played
    private String currSelectedName; //Current name row selected by user
    private RatingManager ratingManager; //Rating instance for rating changes and updates
    private PlayManager playManager;

    //Record sidebar function
    private JFXPopup recordPopup = new JFXPopup();
    private JFXButton playOldButton = new JFXButton(" PLAY OLD");
    private JFXButton playCompare = new JFXButton("COMPARE");
    private JFXButton playNewButton = new JFXButton("PLAY NEW");
    private JFXButton recordSubButton = new JFXButton("RECORD");
    private JFXButton saveButton = new JFXButton("SAVE NEW");
    private String tempAudioName = null; // for recording
    private CreateAudio createAudio;


    @Override
    @FXML
    protected void exitPressed(ActionEvent event) {
        //Delete temp combined names if they exist
        MainController mainController = new MainController();
        mainController.deleteTemp();

        loader.load(FXMLResource.PRACTISE, new StackPane(), mainRoot);
    }

    //Help dialog loads
    @Override
    @FXML
    protected void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane, 2);
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
        playLabel.setText("CURRENTLY SELECTED: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);
        ratingManager.updateRatingComponent(currentNameNum);

        //Disable/Enable rating depending on name concat
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        ratingManager.checkConcatRating(currSelectedName, audioRating);
    }

    //Switches back to previous name audio if there exists previous audio
    @FXML
    void prevPressed(ActionEvent event) {
        //Switching to prev selected audio files
        currentNameNum--;
        if (currentNameNum == 0) {
            prevButton.setDisable(true);
        }
        nextButton.setDisable(false);
        playLabel.setText("CURRENTLY SELECTED: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        nameTable.getSelectionModel().select(currentNameNum);
        ratingManager.updateRatingComponent(currentNameNum);

        //Disable/Enable rating depending on name concat
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        ratingManager.checkConcatRating(currSelectedName, audioRating);
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
        playLabel.setText("CURRENTLY SELECTED: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
        ratingManager.updateRatingComponent(currentNameNum);

        //Disable/Enable rating depending on name concat
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        ratingManager.checkConcatRating(currSelectedName, audioRating);
    }

    //Plays current selected name audio for 5 seconds
    @FXML
    void playPressed(ActionEvent event) {
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName()); //Change text to playing auddio

        new Thread(() ->
                playManager.playOldAudio(practiseController, currentNameNum)).start();
        if (currSelectedName.contains(" ")) {
            UserUtils.updateUser(user, Points.PRACTISE_CONCAT_NAME, userText, pointsText);
        } else {
            UserUtils.updateUser(user, Points.PRACTISE_NAME, userText, pointsText);
        }
    }

    //Creates a popup menu with record,compare plays, and save buttons for user to navigate
    @FXML
    void recordPressed(ActionEvent event) {
        //Button sizes and style
        recordSubButton.setMinSize(130.0, 40);
        playNewButton.setMinSize(130.0, 40);
        playOldButton.setMinSize(130.0, 40);
        playCompare.setMinSize(130.0, 40);
        saveButton.setMinSize(130.0, 40);

        //Setting popup position and size
        recordPopup.show(recordButton, JFXPopup.PopupVPosition.BOTTOM, JFXPopup.PopupHPosition.LEFT, 80, 0);
        recordPopup.getPopupContent().setPrefWidth(130);
    }

    //Stop currently playing name (in case user doesn't want to wait 5 seconds)
    @FXML
    void stopPressed(ActionEvent event) {
        playManager.stopAudio();
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    /**
     * populateTableView: populates users selected Play List in the Practice Controller - then forms table with appropriate details
     */
    private void populateTableView() {
        //Create selectedList from playList in practiseController
        selectedList.addAll(practiseController.getNamePlaylist());

        //Set onto table using selectedList
        nameTable.setItems(selectedList);
        nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
        createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("createdDesc"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));
    }

    public void init() {
        //Setting table and rating updates using RatingManager class
        populateTableView();

        ratingManager = new RatingManager(selectedList, practiseController, audioRating);
        selectedList = ratingManager.ratingUpdate();
        ratingManager.updateRatingComponent(currentNameNum);
        playLabel.setText("CURRENTLY SELECTED: " + practiseController.getNamePlaylist().get(currentNameNum).getName()); //Set text to what name is selected

        //Accounting for single audio in which button is disabled
        if (practiseController.getNamePlaylist().size() == 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(true);
        stopButton.setDisable(true);
        nameTable.getSelectionModel().select(currentNameNum);
        currSelectedName = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";

        //Concat multiple names selected
        new Thread(() -> {
            for (PlayAudio audio : practiseController.getPlayAudioList()) {
                audio.concatCombinedAudio();
            }
        }).start();

        //Disable/Enable rating depending on name concat
        currSelectedName = nameTable.getSelectionModel().getSelectedItem().toString();
        ratingManager.checkConcatRating(currSelectedName, audioRating);

        //When user selects rating, update
        audioRating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            currSelectedName = ratingManager.ratingPressed(newValue.toString(), currentNameNum);
            selectedList = ratingManager.ratingUpdate();
        });

        //JFXPOPUP BUTTON ACTIONS
        //PLAYOLD - plays current names audio file
        saveButton.setDisable(true);
        playOldButton.setOnMousePressed(event -> {
            playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());

            new Thread(() -> playManager.playOldAudio(practiseController, currentNameNum)).start();
            if (currSelectedName.contains(" ")) {
                UserUtils.updateUser(user, Points.PRACTISE_CONCAT_NAME, userText, pointsText);
            } else {
                UserUtils.updateUser(user, Points.PRACTISE_NAME, userText, pointsText);
            }
        });

        //PLAYNEW - plays users recorded version of name file (if exists)
        playNewButton.setDisable(true);
        playNewButton.setOnMousePressed(event -> {
            playLabel.setText("CURRENTLY PLAYING *NEW*: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
            new Thread(() -> playManager.playNewAudio(tempAudioName)).start();
        });

        if (currSelectedName.contains(" ")) {
            UserUtils.updateUser(user, Points.PRACTISE_CONCAT_NAME, userText, pointsText);
        } else {
            UserUtils.updateUser(user, Points.PRACTISE_NAME, userText, pointsText);
        }


        //PLAYCOMPARE - plays comparison between old and new
        playCompare.setDisable(true);
        playCompare.setOnMousePressed(event -> {
            playLabel.setText("CURRENTLY COMPARING: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
            new Thread(() ->
                    playManager.playAlternateAudio(practiseController, currentNameNum, tempAudioName)
            ).start();
            if (currSelectedName.contains(" ")) {
                UserUtils.updateUser(user, Points.COMPARE_NAME, userText, pointsText);
            } else {
                UserUtils.updateUser(user, Points.PRACTISE_NAME, userText, pointsText);
            }
        });

        //RECORD - records user trying to pronounce PLAYOLD name
        recordSubButton.setOnMousePressed(event -> {
            playNewButton.setDisable(false);
            saveButton.setDisable(false);
            playCompare.setDisable(true);

            //Setup official name for saved recording
            new Thread(() -> {
                Platform.runLater(() -> {
                    //Disable buttons
                    playNewButton.setDisable(true);
                    playOldButton.setDisable(true);
                    recordSubButton.setDisable(true);
                    saveButton.setDisable(true);
                    playButton.setDisable(true);
                    playLabel.setText("CURRENTLY RECORDING: " + practiseController.getNamePlaylist().get(currentNameNum).getName()); //Change label on record

                    tempAudioName = user.getUsername() + "_" + practiseController.getNamePlaylist().get(currentNameNum).replaceDesc();
                    createAudio = new CreateAudio(tempAudioName);
                    createAudio.createSingleAudio();
                });

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    //Reenable buttons
                    playNewButton.setDisable(false);
                    playOldButton.setDisable(false);
                    recordSubButton.setDisable(false);
                    saveButton.setDisable(false);
                    playButton.setDisable(false);
                    playCompare.setDisable(false);
                    playCompare.setDisable(false);

                    playLabel.setText("FINISHED RECORDING: " + practiseController.getNamePlaylist().get(currentNameNum).getName()); //Change label on record finished

                    //Disable rating and save buttons for concat
                    if (currSelectedName.contains(" ")) {
                        saveButton.setDisable(true);
                    }
                });
            }).start();
        });

        //SAVE NEW - user attempt placed into database to add onto filtering
        saveButton.setOnMousePressed(event -> {
            saveButton.setDisable(true);
            playLabel.setText("SAVED: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
            createAudio.saveAudio();
            UserUtils.updateUser(user, Points.CREATE_NAME, userText, pointsText);
        });

        //INIT JFX-POPUP
        VBox box = new VBox(recordSubButton, playOldButton, playNewButton, playCompare, saveButton); // can make HBox
        recordPopup.setPopupContent(box);

        //Call icon loader for button icons
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton,
                playButton,  stopButton,  prevButton,  nextButton,  recordButton,
                recordSubButton,  playOldButton, playNewButton,  playCompare,  saveButton);
        iconLoader.loadMenuIcons();
        iconLoader.loadPlayMenuIcons();

        playManager = new PlayManager(playButton, recordButton, stopButton);
    }
}