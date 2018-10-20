package namesayer.login;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * NewUserDialog: Initialises a dialog box where user can create their new profile given that they have entered a correct
 * username and presses create button
 */
public class NewUserDialog implements Initializable {
    private Button buttonCreate = new Button("Create");
    private Button buttonCancel = new Button("Cancel");
    private JFXTextField newUserText = new JFXTextField();
    private JFXListView<User> userList;

    public NewUserDialog(JFXListView<User> userList) {
        this.userList = userList;
    }

    //Creates dialog to make new user
    public void showNewUserDialog(StackPane stackPane) {
        //Components within dialog
        Label label = new Label("New Username: ");
        HBox buttonBox = new HBox(buttonCreate,buttonCancel);
        HBox bodyBox = new HBox(label, newUserText);

        //Set components onto JFXDialog
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        dialogLayout.setHeading(new Label("New User"));
        dialogLayout.setBody(bodyBox);
        dialogLayout.setActions(buttonBox);
        dialog.show();

        //Setting actions to dialog buttons and textField
        buttonCreate.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
            UserUtils.createUser(newUserText.getText().replace(" ",""));
            UserUtils.updateUserList(userList);
            dialog.close();
        });

        buttonCancel.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
            dialog.close();
        });

        //Styling components
        buttonBox.setSpacing(5);
        label.setMinHeight(25);
        newUserText.setMinWidth(220);
        newUserText.setPromptText("Spaces are unsupported");
        newUserText.setFocusColor(Color.web("#ff5252"));
        newUserText.setStyle("-fx-prompt-text-fill: gray;");
        buttonCreate.getStylesheets().add("namesayer/resources/stylesheet/general.css");
        buttonCancel.getStylesheets().add("namesayer/resources/stylesheet/general.css");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // disable button if field is empty
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> newUserText.getText().trim().isEmpty(), newUserText.textProperty());
        buttonCreate.disableProperty().bind(isInvalid);
    }
}
