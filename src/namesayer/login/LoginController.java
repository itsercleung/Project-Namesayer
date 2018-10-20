package namesayer.login;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * LoginController: Deals with initialisation and actions of the login screen on startup - includes handling users and
 * new users and entrance to main controller in namesayer
 */
public class LoginController implements Initializable {

    @FXML private JFXListView<User> userList;
    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Button loginButton;

    @FXML
    private void exitButtonPressed(ActionEvent event) {
        System.exit(0);
    }

    // gets currently selected name to login to
    @FXML
    void loginButtonClicked(ActionEvent event) {
        UserUtils.setCurrentLoginUser(userList.getSelectionModel().getSelectedItem());

        StackPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    //Load new user pane for users to create new profile
    @FXML
    void newUserButtonClicked(ActionEvent event) {
        NewUserDialog newUserDialog = new NewUserDialog(userList);
        newUserDialog.showNewUserDialog(stackPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize user list
        userList.setExpanded(true);
        userList.setDepth(1);
        UserUtils.updateUserList(userList);
        userList.setCellFactory(param -> new UserCell());
        loginButton.disableProperty().bind(userList.getSelectionModel().selectedItemProperty().isNull());
    }
}
