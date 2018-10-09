package namesayer.login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import namesayer.login.User;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginController implements Initializable {

    @FXML
    private ListView<User> userList;
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button loginButton;
    @FXML
    private Button newUserButton;

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
        try {
            mainRoot = FXMLLoader.load(getClass().getResource("../resources/NewUser.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Customer Manager");
            stage.setScene(new Scene(mainRoot));
            stage.setOnHiding(eventClosed ->
                    UserUtils.getUserList(userList));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting initial params
        File directory = new File("./data/usernames");
        File[] dirFiles = directory.listFiles();
        ObservableList<User> users = FXCollections.observableArrayList();

        UserUtils.getUserList(userList);
        loginButton.disableProperty().bind(userList.getSelectionModel().selectedItemProperty().isNull());
    }
}
