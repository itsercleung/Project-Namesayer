package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

/**
 * NameSayerMenuController is the common implementation for all menus
 * of NameSayer.
 */
public abstract class NameSayerMenuController implements Initializable {
    @FXML protected AnchorPane mainRoot;
    @FXML protected StackPane stackPane;
    @FXML protected Text userText, pointsText;
    @FXML protected Button helpButton, rewardButton, exitButton;

    protected User user;
    protected FXMLResourceLoader loader = new FXMLResourceLoader();

    /**
     * Logout button pressed
     * @param event
     */
    @FXML protected void exitPressed(ActionEvent event) {
        loader.load(FXMLResource.LOGOUT, new StackPane(), mainRoot);
    }

    /**
     * Loads the general help popup
     */
    @FXML protected void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane, 1);
    }

    /**
     * Load rewards menu
     */
    @FXML protected void rewardPressed(ActionEvent event) {
        loader.load(FXMLResource.REWARD, new StackPane(), mainRoot);
    }

    /**
     * Loads the test microphone menu
     */
    @FXML protected void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        loader.load(FXMLResource.TEST_MICROPHONE,new StackPane(),mainRoot);
    }

    /**
     * Load practise pane
     */
    @FXML protected void practisePressed(ActionEvent event) {
        loader.load(FXMLResource.PRACTISE,new StackPane(),mainRoot);
    }

    /**
     * record new practise pane
     */
    @FXML protected void recordNamePressed(ActionEvent event) {
        loader.load(FXMLResource.RECORD_NEW,new StackPane(),mainRoot);
    }

    /**
     * Starts the controller with the initialize functions
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = UserUtils.getCurrentLoginUser(userText, pointsText); //Set user current name and score
        init();
    }

    /**
     * init method is where you implement important code when the Controller
     * is being accessed.
     */
    public abstract void init();

}
