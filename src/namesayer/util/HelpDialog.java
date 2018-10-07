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

        dialogLayout.setHeading(new Label("?"));
        Label pracHead = new Label("Practice: ");
        Label pracBody = new Label("(1) Search for name (or combined) or Upload txt \n" +
                "(2) Decide to Randomize PLAYLIST \n" +
                "(3) Click Practice and Enjoy!");
        dialogLayout.setBody(pracHead);
        dialogLayout.setBody(pracBody);
        dialogLayout.setActions(button);
        dialog.show();

        //Label styles
        pracHead.setStyle("-fx-font-size: 14px;" + "-fx-font-style: bold");
        pracBody.setStyle("-fx-font-size: 13px;");
    }
}
