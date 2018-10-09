package namesayer.util;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class HelpDialog {

    public void showHelpDialog(StackPane stackPane) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        Button button = new Button("Got it!");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);

        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
            dialog.close();
        });

        dialogLayout.setHeading(new Label("Tips"));
        Label pracBody = new Label("Practice: \n" +
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
        dialogLayout.setBody(pracBody);
        dialogLayout.setActions(button);
        dialog.show();

        //Label styles
        pracBody.setStyle("-fx-font-size: 14px;");
        button.getStylesheets().add("namesayer/resources/stylesheet/general.css");
    }
}
