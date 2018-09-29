package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import namesayer.login.User;

public class LoginController {

    @FXML
    private ListView<User> userList;

    @FXML
    private AnchorPane root;

    @FXML
    private Button loginButton;

    @FXML
    private Button newUserButton;

    @FXML
    void loginButtonClicked(ActionEvent event) {

    }

    @FXML
    void newUserButtonClicked(ActionEvent event) {

    }

}
