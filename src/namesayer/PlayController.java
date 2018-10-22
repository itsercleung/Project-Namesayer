package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import namesayer.login.Points;
import namesayer.login.UserUtils;
import namesayer.util.CreateAudio;
import namesayer.util.HelpDialog;
import namesayer.util.IconLoader;
import namesayer.util.Name;
import namesayer.util.fxmlloader.FXMLResource;
import namesayer.util.play.PlayAudio;
import namesayer.util.play.PlayManager;
import namesayer.util.play.PlayUtils;
import namesayer.util.play.RatingManager;
import org.controlsfx.control.Rating;

/**
 * PlayController : Deals with users selected playList to practise names in. Using such playList it updates an existing
 * table with its specified name, creator and rating. It provides play components, recording components and dynamic name
 * changing components.
 * @author Eric Leung, Kevin Xu
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
    private PlayUtils playUtils;

    @Override
    @FXML
    protected void exitPressed(ActionEvent event) {
        //Delete temp combined names if they exist
        MainController mainController = new MainController();
        mainController.deleteTemp();

        loader.load(FXMLResource.PRACTISE, new StackPane(), mainRoot);
    }

    /**
     * Help dialog loads
     */
    @Override
    @FXML
    protected void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane, 2);
    }

    /**
     * Switches to next name audio if there exists next audio
     */
    @FXML
    void nextPressed(ActionEvent event) {
        playUtils.nextPressed(prevButton,nextButton,nameTable,ratingManager,audioRating);
    }

    /**
     * Switches back to previous name audio if there exists previous audio
     */
    @FXML
    void prevPressed(ActionEvent event) {
        playUtils.prevPressed(prevButton,nextButton,nameTable,ratingManager,audioRating);
    }

    /**
     * Changes current audio to whatever user selects
     */
    @FXML
    void rowClicked(MouseEvent event) {
        playUtils.rowClicked(nameTable,selectedList,prevButton,nextButton,ratingManager,audioRating);
    }

    /**
     * Plays current selected name audio for 5 seconds
     */
    @FXML
    void playPressed(ActionEvent event) {
        playLabel.setText("CURRENTLY PLAYING: " + practiseController.getNamePlaylist().get(playUtils.getCurrentNameNum()).getName()); //Change text to playing auddio

        new Thread(() ->
                playManager.playOldAudio(practiseController, playUtils.getCurrentNameNum())).start();
        if (currSelectedName.contains(" ")) {
            UserUtils.updateUser(user, Points.PRACTISE_CONCAT_NAME, userText, pointsText);
        } else {
            UserUtils.updateUser(user, Points.PRACTISE_NAME, userText, pointsText);
        }
    }

    /**
     * Creates a popup menu with record,compare plays, and save buttons for user to navigate
     */
    @FXML
    void recordPressed(ActionEvent event) {
        playUtils.recordPopup(recordPopup,recordButton);
    }

    /**
     * Stop currently playing name (in case user doesn't want to wait 5 seconds)
     */
    @FXML
    void stopPressed(ActionEvent event) {
        playManager.stopAudio();
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    public void init() {
        //Setting table and rating updates using RatingManager class
        playUtils = new PlayUtils();
        playUtils.populateTableView(practiseController,selectedList,nameTable,nameCol,createdCol,ratingCol);

        ratingManager = new RatingManager(selectedList, practiseController, audioRating);
        selectedList = ratingManager.ratingUpdate();
        ratingManager.updateRatingComponent(playUtils.getCurrentNameNum());
        playLabel.setText("CURRENTLY SELECTED: " + practiseController.getNamePlaylist().get(0).getName()); //Set text to what name is selected

        //Accounting for single audio in which button is disabled
        if (practiseController.getNamePlaylist().size() == 1) {
            nextButton.setDisable(true);
        }
        prevButton.setDisable(true);
        stopButton.setDisable(true);
        nameTable.getSelectionModel().select(playUtils.getCurrentNameNum());
        currSelectedName = practiseController.getNamePlaylist().get(0).toString() + ".wav";

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
            currSelectedName = ratingManager.ratingPressed(newValue.toString(), playUtils.getCurrentNameNum());
            selectedList = ratingManager.ratingUpdate();
        });

        //Instantiate manager and utils for play scene
        playManager = new PlayManager(playButton, recordButton, stopButton);
        playUtils = new PlayUtils(saveButton,playOldButton,playNewButton,playCompare,
                recordSubButton,user,playLabel,userText,pointsText,
                currSelectedName,playManager,practiseController,0,
                tempAudioName,playButton,createAudio);
        playUtils.popupButtons();

        //INIT JFX-POPUP
        VBox box = new VBox(recordSubButton, playOldButton, playNewButton, playCompare, saveButton); // can make HBox
        recordPopup.setPopupContent(box);

        //Call icon loader for button icons
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton,
                playButton,  stopButton,  prevButton,  nextButton,  recordButton,
                recordSubButton,  playOldButton, playNewButton,  playCompare,  saveButton);
        iconLoader.loadMenuIcons();
        iconLoader.loadPlayMenuIcons();
    }
}