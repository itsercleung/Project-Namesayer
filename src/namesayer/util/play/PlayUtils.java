package namesayer.util.play;

import com.jfoenix.controls.JFXPopup;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import namesayer.PractiseController;
import namesayer.login.Points;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.CreateAudio;
import namesayer.util.Name;
import org.controlsfx.control.Rating;

/**
 * PlayUtils: Deals with the button logic in the PlayController
 */
public class PlayUtils {

    private Button saveButton, playOldButton, playNewButton,
            playCompare, recordSubButton;
    private User user;
    private Label playLabel;
    private Text userText, pointsText;
    private String currSelectedName;
    private PlayManager playManager;
    private PractiseController practiseController;
    private int currentNameNum;
    private String tempAudioName;
    private Button playButton;
    private CreateAudio createAudio;

    public PlayUtils() {
    }

    public PlayUtils(Button saveButton, Button playOldButton, Button playNewButton,
                     Button playCompare, Button recordSubButton,
                     User user, Label playLabel, Text userText, Text pointsText,
                     String currSelectedName, PlayManager playManager,
                     PractiseController practiseController, int currentNameNum,
                     String tempAudioName, Button playButton, CreateAudio createAudio) {
        this.saveButton = saveButton;
        this.playOldButton = playOldButton;
        this.playNewButton = playNewButton;
        this.playCompare = playCompare;
        this.recordSubButton = recordSubButton;
        this.user = user;
        this.playLabel = playLabel;
        this.userText = userText;
        this.pointsText = pointsText;
        this.currSelectedName = currSelectedName;
        this.playManager = playManager;
        this.practiseController = practiseController;
        this.currentNameNum = currentNameNum;
        this.tempAudioName = tempAudioName;
        this.playButton = playButton;
        this.createAudio = createAudio;
    }

    /**
     * popupButtons: handles record popup button functions
     */
    public void popupButtons() {
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
            playCompare.setDisable(true);
            playNewButton.setDisable(true);
            playLabel.setText("SAVED: " + practiseController.getNamePlaylist().get(currentNameNum).getName());
            createAudio.saveAudio();
            UserUtils.updateUser(user, Points.CREATE_NAME, userText, pointsText);
        });
    }

    /**
     * recordPopup: Record popup appears with given buttons and layout
     *
     * @param recordPopup
     * @param recordButton
     */
    public void recordPopup(JFXPopup recordPopup, Button recordButton) {
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

    /**
     * rowClicked: on tables row click allows changes the names current selected name to be played
     */
    public void rowClicked(TableView<Name> nameTable,
                           ObservableList<Name> selectedList,
                           Button prevButton,
                           Button nextButton,
                           RatingManager ratingManager,
                           Rating audioRating) {
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

    /**
     * prevPressed: Switching to prev selected audio files if previous button is pressed
     */
    public void prevPressed(Button prevButton, Button nextButton, TableView<Name> nameTable,
                            RatingManager ratingManager, Rating audioRating) {
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

    /**
     * nextPressed: Switching to next selected audio files if next button is pressed
     */
    public void nextPressed(Button prevButton, Button nextButton, TableView<Name> nameTable,
                            RatingManager ratingManager, Rating audioRating) {
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

    /**
     * populateTableView: Populates table view on play controller given the selected names playlist
     */
    public void populateTableView(PractiseController practiseController,
                                  ObservableList<Name> selectedList,
                                  TableView<Name> nameTable,
                                  TableColumn<Name, String> nameCol,
                                  TableColumn<Name, String> createdCol,
                                  TableColumn<Name, Rating> ratingCol) {
        //Create selectedList from playList in practiseController
        selectedList.addAll(practiseController.getNamePlaylist());

        //Set onto table using selectedList
        nameTable.setItems(selectedList);
        nameCol.setCellValueFactory(new PropertyValueFactory<Name, String>("name"));
        createdCol.setCellValueFactory(new PropertyValueFactory<Name, String>("createdDesc"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<Name, Rating>("rating"));
    }

    /**
     * Gets the current integer position of current name to be played
     *
     * @return Current name position in selected playlist
     */
    public int getCurrentNameNum() {
        return currentNameNum;
    }
}
