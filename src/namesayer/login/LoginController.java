package namesayer.login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import namesayer.login.User;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LoginController implements Initializable {

    @FXML private ListView<User> userList;
    @FXML private AnchorPane mainRoot;
    @FXML private Button loginButton;
    @FXML private Button newUserButton;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        // gets currently selected name to login to
        AnchorPane practiseRoot = null;
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
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setting initial params
        loginButton.setDisable(true);
        File directory = new File("./data/usernames");
        File[] dirFiles = directory.listFiles();
        ObservableList<User> users = FXCollections.observableArrayList();
        Scanner reader;

        //If user selects username from listView
        userList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                loginButton.setDisable(false);

            }
        });

        if (dirFiles != null) {
            for (File file : dirFiles) {
                try {
                    // gets user name and points, adds to listview
                    reader = new Scanner(file);
                    String username = reader.next();
                    int points = Integer.valueOf(reader.next());
                    users.add(new User(username,points));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            userList.setItems(users);
        }
    }
}
