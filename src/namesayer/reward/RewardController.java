package namesayer.reward;

import com.jfoenix.controls.JFXListView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    @FXML private Text userText, pointsText;
    @FXML private Button exitButton, helpButton, rewardButton, redeemButton, applyButton;
    @FXML private Label pointsToSpend;
    @FXML private JFXListView<Reward> rewardList;

    private FXMLResourceLoader loader = new FXMLResourceLoader();
    private User user;
    private RewardManager rb;

    @FXML
    void exitPressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.LOGOUT, root, mainRoot);
    }

    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane);
    }

    @FXML
    void practisePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.PRACTISE, root, mainRoot);
    }

    @FXML
    void recordNamePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.RECORD_NEW, root, mainRoot);
    }

    @FXML
    void testMicrophonePressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.TEST_MICROPHONE, root, mainRoot);
    }

    @FXML
    void rewardPressed(ActionEvent event) {
        StackPane root = null;
        loader.load(FXMLResource.REWARD, root, mainRoot);
    }

    @FXML
    void applyPressed(ActionEvent event) {
        rb.applyReward(rewardList.getSelectionModel().getSelectedItem());
    }

    @FXML
    void redeemPressed(ActionEvent event) {
        rb.redeemReward(rewardList.getSelectionModel().getSelectedItem());
    }

    @FXML
    void rewardRowClicked(MouseEvent event) {
        //Determine if awards/achievements are redeemable (ie unlock-able through store rather than set num of points)
        if (!rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
            redeemButton.setDisable(true);
            applyButton.setDisable(true);
        }
        else if (rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
            redeemButton.setDisable(false);
            applyButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rewardButton.setDisable(true);

        //Set up user info
        user = UserUtils.getCurrentLoginUser(userText, pointsText);

        //Sets up rewards to populate reward list
        rb = new RewardManager(user);
        rewardList.setItems(rb.build());
        rewardList.setCellFactory(param -> new RewardCell(user));

        //Set up display of points
        String text = "You have earned " + user.getPoints() + " points!";
        pointsToSpend.setText(text);

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
