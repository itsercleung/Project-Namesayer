package namesayer.util;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.print.PageLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HelpDialog {
    private Button helpButton;

    public HelpDialog(Button helpButton) {
        this.helpButton = helpButton;
        helpButton.setDisable(true);
    }

    public void showHelpDialog(StackPane stackPane, int identifierVal) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        Button button = new Button("Got it!");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
            dialog.close();
        });

        dialog.setOnDialogClosed(closeEvent -> {
            helpButton.setDisable(false);
        });

        dialogLayout.setHeading(new Label("Tips"));

        //Using identifier val to set up label type
        Label pracBody = new Label("");

        if (identifierVal == 1) {
            pracBody = new Label("Practice: \n" +
                    "(1) Search for name (or combined) or Upload txt \n" +
                    "(2) Decide to Randomize PLAYLIST \n" +
                    "(3) Click Practice and Enjoy! \n" +
                    "\n" +
                    "Test Microphone: \n" +
                    "(1) Press Test button to record \n" +
                    "(2) Look at mic level \n" +
                    "(3) Listen to your test record\n" +
                    "\n" +
                    "Record New Name: \n" +
                    "(1) Enter a valid name \n" +
                    "(2) Press Record button to record \n" +
                    "(3) Listen/Save recording");
        }
        else if (identifierVal == 2) {
            pracBody = new Label("Play name: \n" +
                    "(1) Select name (red outline) by navigating with buttons or clicking on name \n" +
                    "(2) Press PLAY symbol below \n" +
                    "(3) Listen \n" +
                    "\n" +
                    "Record name: \n" +
                    "(1) Select name (red outline) you want to record for \n" +
                    "(2) Press RECORD (RED CIRCLE) symbol below \n" +
                    "(3) Press RECORD on popup (and listen or save)");
        }
        else if (identifierVal == 3) {
            pracBody = new Label( "Reward system: \n" +
                    "* You will unlock achievements and rewards through \n" +
                    "  LISTENING TO NAMES and SAVING RECORDINGS \n" +
                    "# Achievements and rewards can be displayed on profile \n" +
                    "  picture on login \n" +
                    "Apply reward: \n" +
                    "(1) Select which reward to apply (red selection) \n" +
                    "(2) Click APPLY button below \n" +
                    "(3) Done! See changes in login screen!");
        }

        dialogLayout.setBody(pracBody);
        dialogLayout.setActions(button);
        dialog.show();

        //Label styles
        pracBody.setStyle("-fx-font-size: 14px;");
        button.getStylesheets().add("namesayer/resources/stylesheet/general.css");
    }
}
