package namesayer.reward;

import com.jfoenix.controls.JFXListView;
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
import namesayer.util.HelpDialog;

import java.io.IOException;
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
    @FXML private JFXListView<Reward> rewardList;

    @FXML
    void applyPressed(ActionEvent event) {

    }

    @FXML
    void exitPressed(ActionEvent event) {
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
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    @FXML
    void practisePressed(ActionEvent event) {

    }

    @FXML
    void recordNamePressed(ActionEvent event) {

    }

    @FXML
    void redeemPressed(ActionEvent event) {

    }

    @FXML
    void rewardPressed(ActionEvent event) {

    }

    @FXML
    void testMicrophonePressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rewardButton.setDisable(true);

        rewardList.setItems(RewardBuilder.build()); // reward builder is location of all reward types
        rewardList.setCellFactory(param -> new RewardCell());

        // Reward and help Popup icons
        Image reward = new Image(getClass().getResourceAsStream("../resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("../resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));
        Image help = new Image(getClass().getResourceAsStream("../resources/icons/info.png"));
        helpButton.setGraphic(new ImageView(help));
        Image logout = new Image(getClass().getResourceAsStream("../resources/icons/sign-out.png"));
        exitButton.setGraphic(new ImageView(logout));
    }
}
