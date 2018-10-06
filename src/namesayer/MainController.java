package namesayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import namesayer.login.User;
import namesayer.login.UserUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {

    @FXML
    private AnchorPane mainRoot;
    @FXML
    private Text userText, pointsText;
    @FXML
    private Button homeButton;
    @FXML
    private ListView<User> userList;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        // gets currently selected name to login to
        User user = userList.getSelectionModel().getSelectedItem();
        UserUtils.setCurrentLoginUser(user);
        UserUtils.updateUser(userText,pointsText,user);
    }

    @FXML
    void newUserButtonClicked(ActionEvent event) {
        //Load new user pane
        AnchorPane newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("./resources/NewUser.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(newRoot);
    }

    @FXML
    private void homePressed(ActionEvent event) {

    }

    @FXML
    private void exitPressed(ActionEvent event) {
        //System.exit(0);
        // testing login menu
        AnchorPane testMicrophoneRoot = null;
        try {
            testMicrophoneRoot = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(testMicrophoneRoot);
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

    public void deleteTemp() {
        //Delete temp files on startup
        File temp = new File("temp/");
        if (temp.exists() && temp.isDirectory()) {
            String[] entries = temp.list();
            for (String s : entries) {
                File currentFile = new File(temp.getPath(), s);
                currentFile.delete();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeButton.setDisable(true);
        deleteTemp(); //On startup delete temp files in folder

        //If txt doesnt exist then make one and append TITLE
        File pqFile = new File("data/ratingAudio.txt");
        if (!pqFile.exists()) {
            try {
                pqFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Creating temp folder and data folder
        new File("./temp").mkdirs();
        new File("./data").mkdirs();
        new File("./data/names").mkdirs();

        UserUtils.getUserList(userList);
    }

}
