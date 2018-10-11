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
import namesayer.util.HelpDialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Text userText, pointsText;
    @FXML private Button helpButton, rewardButton;

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

    //Load help popup
    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    //Load rewards window
    @FXML
    private void rewardPressed(ActionEvent event) {
        StackPane rewardsRoot = null;
        try {
            rewardsRoot = FXMLLoader.load(getClass().getResource("resources/Reward.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(rewardsRoot);
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

        //Set user current name and score
        user = UserUtils.getCurrentLoginUser(userText,pointsText);

        // Reward and help Popup icons
        Image reward = new Image(getClass().getResourceAsStream("resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));
        Image help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));
        helpButton.setGraphic(new ImageView(help));
    }
}
