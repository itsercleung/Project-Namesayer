package namesayer.reward;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import namesayer.util.FXMLResourceLoader;
import namesayer.util.HelpDialog;

import java.net.URL;
import java.util.ResourceBundle;

public class RewardController implements Initializable {

    @FXML private StackPane stackPane;
    @FXML private AnchorPane mainRoot;
    @FXML private Text userText,pointsText;
    @FXML private Button exitButton,helpButton,rewardButton;
    @FXML private Button redeemButton,applyButton;
    @FXML private JFXListView<Reward> rewardList;
    private FXMLResourceLoader loader = new FXMLResourceLoader();
    private User user;

    @FXML
    void exitPressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.LOGOUT,root,mainRoot);
    }

    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    @FXML
    void practisePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.PRACTISE,root,mainRoot);
    }

    @FXML
    void recordNamePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.RECORD_NEW,root,mainRoot);
    }

    @FXML
    void testMicrophonePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.TEST_MICROPHONE,root,mainRoot);
    }

    @FXML
    void rewardPressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.REWARD,root,mainRoot);
    }

    @FXML
    void applyPressed(ActionEvent event) {

    }

    @FXML
    void redeemPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rewardButton.setDisable(true);

        rewardList.setItems(RewardBuilder.build());
        rewardList.setCellFactory(param -> new RewardCell());

        user = UserUtils.getCurrentLoginUser(userText,pointsText);

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
