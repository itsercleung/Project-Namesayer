package namesayer.reward;

import com.jfoenix.controls.JFXListView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.IconLoader;
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
    @FXML private Button exitButton, helpButton, rewardButton, applyButton;
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
        helpDialog.showHelpDialog(stackPane ,3);
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
        Reward reward = rewardList.getSelectionModel().getSelectedItem();
        rb.applyReward(reward);

        // TODO apply different trophy logic
        // TODO disable buttons immediately after applying reward
        StackPane root = null;
        loader.load(FXMLResource.REWARD,root,mainRoot);
    }

    // TODO possible remove redeem feature, as there is no point anymore
    // TODO we have implemented an auto checker when points threshold is reached
    /*@FXML
    void redeemPressed(ActionEvent event) {
        Reward reward = rewardList.getSelectionModel().getSelectedItem();
        rb.redeemReward(reward);
    }*/

    @FXML
    void rewardRowClicked(MouseEvent event) {
        // TODO bug where you can use arrow keys to select row to unlock!
        //Determine if awards/achievements are redeemable (ie unlock-able through store rather than set num of points)
        if (!rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
            applyButton.setDisable(true);
        }
        else if (rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
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
        IconLoader iconLoader = new IconLoader(rewardButton,helpButton,exitButton);
        iconLoader.loadMenuIcons();
    }
}
