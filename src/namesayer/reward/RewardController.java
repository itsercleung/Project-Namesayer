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
        loader.load(FXMLResource.LOGOUT, new StackPane(), mainRoot);
    }

    @FXML
    void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane ,3);
    }

    @FXML
    void practisePressed(ActionEvent event) {
        loader.load(FXMLResource.PRACTISE, new StackPane(), mainRoot);
    }

    @FXML
    void recordNamePressed(ActionEvent event) {
        loader.load(FXMLResource.RECORD_NEW, new StackPane(), mainRoot);
    }

    @FXML
    void testMicrophonePressed(ActionEvent event) {
        loader.load(FXMLResource.TEST_MICROPHONE, new StackPane(), mainRoot);
    }

    @FXML
    void rewardPressed(ActionEvent event) {
        loader.load(FXMLResource.REWARD, new StackPane(), mainRoot);
    }

    @FXML
    void applyPressed(ActionEvent event) {
        Reward reward = rewardList.getSelectionModel().getSelectedItem();
        rb.applyReward(reward);
        loader.load(FXMLResource.REWARD,new StackPane(),mainRoot);
        applyButton.setDisable(true);
    }

    @FXML
    void rewardRowClicked(MouseEvent event) {
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
        applyButton.setDisable(true);

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
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton);
        iconLoader.loadMenuIcons();
    }
}
