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

        dialogLayout.setHeading(new Label("Help"));
        dialogLayout.setActions(button);
        dialog.show();
    }
}
