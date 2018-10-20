package namesayer;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import namesayer.util.*;
import namesayer.util.fxmlloader.FXMLResource;
import namesayer.util.practise.PractiseUtils;
import namesayer.util.practise.SearchChangeListener;
import namesayer.util.practise.SearchTextChangeListener;
import namesayer.util.practise.UploadNameList;
import org.controlsfx.control.ToggleSwitch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PractiseController: Deals with user selecting names and creating them into a whole play list for the PlayController.
 * Users are able to search on click and also provide an input of a text file (given the format) to input into the playList.
 * Users are able to add names that are single names or full names. They are also provided with randomization selection and
 * clear buttons for flexibility.
 */
public class PractiseController extends NameSayerMenuController implements Initializable {

    @FXML
    private ToggleSwitch toggleRandomise;
    @FXML
    private Button playNames, practiseButton, uploadButton;
    @FXML
    private JFXTextField searchTextField;
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXListView<String> searchNamesView, selectedNamesView;

    private ObservableList<String> searchNameList = FXCollections.observableArrayList(); //List of all names
    private ObservableList<String> selectedNameList = FXCollections.observableArrayList(); //List of playList
    private static List<Name> namePlaylist = new ArrayList<>(); //List of playList (names obj)
    private static List<PlayAudio> playAudioList = new ArrayList<>(); //List of playList (playAudio obj)
    private FilteredList<String> filteredData; //Search list
    private String concatName = "";
    private boolean isRandomised = false;

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

        loader.load(FXMLResource.PLAY, new AnchorPane(), mainRoot);
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
        UploadNameList uploadNameList = new UploadNameList(selectedNameList,
                searchNameList,
                searchNamesView,
                selectedNamesView,
                stackPane,
                searchTextField,
                mainRoot);

        uploadNameList.upload(uploadButton);
    }

    /**
     * getNamePlayList: Used in PlayController to get current selected Playlist of users
     *
     * @return returns list of play list (name obj) for PlayController
     */
    public List<Name> getNamePlaylist() {
        return namePlaylist;
    }

    /**
     * getPlayAudioList: Used to concat existing PlayAudio
     *
     * @return list of playList (play audio obj) for PlayController
     */
    public List<PlayAudio> getPlayAudioList() {
        return playAudioList;
    }

    public void init() {
        //Clear selectedLists and setup components
        namePlaylist.clear();
        selectedNameList.clear();
        playAudioList.clear();
        searchNamesView.setVisible(false);
        practiseButton.setDisable(true);

        //Populate and get information from directory
        PractiseUtils practiseUtils = new PractiseUtils();
        practiseUtils.populateList(searchNameList);

        //Assigns boolean value to isRandomised depending on toggleRandomise component
        toggleRandomise.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isRandomised = true; // on
            } else {
                isRandomised = false; // off
            }
        });

        //Add selected name from search onto selectedNamesList and update such view
        searchNamesView.getSelectionModel().selectedItemProperty().addListener(
                new SearchChangeListener(selectedNameList,
                        searchNameList,
                        searchNamesView,
                        selectedNamesView,
                        stackPane,
                        searchTextField,
                        mainRoot));

        //Filtering and search function
        filteredData = new FilteredList<>(searchNameList, p -> true);

        searchTextField.textProperty().addListener(
                new SearchTextChangeListener(concatName, searchNameList, filteredData,
                        searchNamesView));

        //Adding search results to sortedList
        SortedList<String> sortedList = new SortedList<>(filteredData);
        searchNamesView.setItems(sortedList);

        // bind appropriate conditions to each button
        playNames.disableProperty().bind(Bindings.isEmpty(selectedNameList));
        toggleRandomise.disableProperty().bind(Bindings.size(selectedNameList).lessThan(2));

        // Reward and help Popup icons
        IconLoader iconLoader = new IconLoader(user, rewardButton, helpButton, exitButton);
        iconLoader.loadMenuIcons();
    }

}