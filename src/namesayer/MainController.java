package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.FXMLResource;
import namesayer.util.HelpDialog;
import namesayer.util.FXMLResourceLoader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Text userText, pointsText;
    @FXML private Button helpButton, rewardButton, exitButton;

    private User user;
    private FXMLResourceLoader loader = new FXMLResourceLoader();

    @FXML private void exitPressed(ActionEvent event) {
        loader.load(FXMLResource.LOGOUT, new StackPane(), mainRoot);
    }

    //Load help popup
    @FXML private void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane, 1);
    }

    //Load rewards window
    @FXML private void rewardPressed(ActionEvent event) {
        loader.load(FXMLResource.REWARD, new StackPane(), mainRoot);
    }

    @FXML private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        loader.load(FXMLResource.TEST_MICROPHONE,new StackPane(),mainRoot);
    }

    @FXML private void practisePressed(ActionEvent event) {
        //Load practise pane
        loader.load(FXMLResource.PRACTISE,new StackPane(),mainRoot);
    }

    @FXML private void recordNamePressed(ActionEvent event) {
        //record new practise pane
        loader.load(FXMLResource.RECORD_NEW,new StackPane(),mainRoot);
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

        //Set user current name and score
        user = UserUtils.getCurrentLoginUser(userText,pointsText);

        // Reward and help Popup icons
        IconLoader iconLoader = new IconLoader(rewardButton,helpButton,exitButton);
        iconLoader.loadMenuIcons();
    }
}
