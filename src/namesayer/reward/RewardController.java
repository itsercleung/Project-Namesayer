package namesayer.reward;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.util.FXMLResource;
import namesayer.util.FXMLResourceLoader;
import namesayer.util.HelpDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class RewardController implements Initializable {

    @FXML private StackPane stackPane;
    @FXML private AnchorPane mainRoot;
    @FXML private Text userText;
    @FXML private Text pointsText;
    @FXML private Button rewardButton;
    @FXML private Button practiseButton;
    @FXML private Button testMicrophoneButton;
    @FXML private Button recordNameButton;
    @FXML private Button exitButton;
    @FXML private Button helpButton;
    @FXML private Button redeemButton;
    @FXML private Button applyButton;

    private FXMLResourceLoader loader = new FXMLResourceLoader();

    @FXML
    void applyPressed(ActionEvent event) {

    }

    @FXML private void exitPressed(ActionEvent event) {
        StackPane loginRoot = null;
        loader.load(FXMLResource.LOGOUT, loginRoot, mainRoot);
    }

    //Load help popup
    @FXML private void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    @FXML private void rewardPressed(ActionEvent event) {
    }

    @FXML private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        StackPane testMicrophoneRoot = null;
        loader.load(FXMLResource.TEST_MICROPHONE,testMicrophoneRoot,mainRoot);
    }

    @FXML private void practisePressed(ActionEvent event) {
        //Load practise pane
        StackPane practiseRoot = null;
        loader.load(FXMLResource.PRACTISE,practiseRoot,mainRoot);
    }

    @FXML private void recordNamePressed(ActionEvent event) {
        //record new practise pane
        StackPane practiseRoot = null;
        loader.load(FXMLResource.RECORD_NEW,practiseRoot,mainRoot);
    }

    @FXML
    void redeemPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rewardButton.setDisable(true);

        // Reward and help Popup icons
        Image reward = new Image(getClass().getResourceAsStream("../resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("../resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));
        Image help = new Image(getClass().getResourceAsStream("../resources/icons/info.png"));
        helpButton.setGraphic(new ImageView(help));
    }
}
