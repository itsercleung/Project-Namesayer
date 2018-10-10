package namesayer.login;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private JFXListView<User> userList;
    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Button loginButton;
    @FXML private Button newUserButton;

    @FXML
    private void exitButtonPressed(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        // gets currently selected name to login to
        UserUtils.setCurrentLoginUser(userList.getSelectionModel().getSelectedItem());

        StackPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    @FXML
    void newUserButtonClicked(ActionEvent event) {
        //Load new user pane
        NewUserDialog newUserDialog = new NewUserDialog(userList);
        newUserDialog.showNewUserDialog(stackPane);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize user list
        userList.setExpanded(true);
        userList.setDepth(1);
        UserUtils.getUserList(userList);
        userList.setCellFactory(param -> new UserCell());
        loginButton.disableProperty().bind(userList.getSelectionModel().selectedItemProperty().isNull());
    }
}
