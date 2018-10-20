package namesayer.reward;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.IconLoader;
import namesayer.NameSayerMenuController;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.FXMLResource;
import namesayer.util.FXMLResourceLoader;
import namesayer.util.HelpDialog;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * RewardController: Controller which deals with actions and transitions around the reward scene. Deals with user handling
 * rewards and applying such rewards and initializing users currently earned and locked rewards, including using RewardCell
 * to display all rewards on the listView
 */
public class RewardController extends NameSayerMenuController implements Initializable {

    @FXML private StackPane stackPane;
    @FXML private Button applyButton;
    @FXML private Label pointsToSpend;
    @FXML private JFXListView<Reward> rewardList;

    private RewardManager rb;

    //Display Rewards help dialog
    @Override @FXML
    protected void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane ,3);
    }

    //Given reward is selected, apply reward to users profile.
    @FXML
    void applyPressed(ActionEvent event) {
        Reward reward = rewardList.getSelectionModel().getSelectedItem();
        rb.applyReward(reward);
        loader.load(FXMLResource.REWARD,new StackPane(),mainRoot);
        applyButton.setDisable(true);
    }

    //Determine if awards/achievements are redeemable (ie unlock-able through store rather than set num of points)
    @FXML
    void rewardRowClicked(MouseEvent event) {
        if (!rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
            applyButton.setDisable(true);
        }
        else if (rewardList.getSelectionModel().getSelectedItem().getIsRedeemable()) {
            applyButton.setDisable(false);
        }
    }

    public void init() {
        rewardButton.setDisable(true);
        applyButton.setDisable(true);

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
