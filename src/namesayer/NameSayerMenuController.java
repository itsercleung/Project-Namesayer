package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import namesayer.util.FXMLResource;
import namesayer.util.FXMLResourceLoader;
import namesayer.util.HelpDialog;

/**
 * NameSayerMenuController is the common implementation for all menus
 * of NameSayer.
 */
public abstract class NameSayerMenuController {
    @FXML protected AnchorPane mainRoot;
    @FXML protected StackPane stackPane;
    @FXML protected Text userText, pointsText;
    @FXML protected Button helpButton, rewardButton, exitButton;

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

    //Load practise pane
    @FXML private void practisePressed(ActionEvent event) {
        loader.load(FXMLResource.PRACTISE,new StackPane(),mainRoot);
    }

    //record new practise pane
    @FXML private void recordNamePressed(ActionEvent event) {
        loader.load(FXMLResource.RECORD_NEW,new StackPane(),mainRoot);
    }
}
