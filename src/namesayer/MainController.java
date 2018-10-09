package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.HelpDialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Text userText, pointsText;

    private User user;

    @FXML
    private void exitPressed(ActionEvent event) {
        StackPane loginRoot = null;
        try {
            loginRoot = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(loginRoot);
    }

    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showHelpDialog(stackPane);
    }

    @FXML
    private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        StackPane testMicrophoneRoot = null;
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
        StackPane practiseRoot = null;
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
        StackPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/RecordNew.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    //Method to delete all existing temp files within the folder
    public void deleteTemp() {
        File temp = new File("temp/");
        if (temp.exists() && temp.isDirectory()) {
            String[] entries = temp.list();
            for(String s: entries){
                File currentFile = new File(temp.getPath(),s);
                currentFile.delete();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        new File("./data/usernames").mkdirs();

        user = UserUtils.getCurrentLoginUser(userText,pointsText);
    }
}
