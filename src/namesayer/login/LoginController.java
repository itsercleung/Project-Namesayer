package namesayer.login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
    private AnchorPane root;

    @FXML
    private Button loginButton;

    @FXML
    private Button newUserButton;

    @FXML
    void loginButtonClicked(ActionEvent event) {
        // gets currently selected name to login to

    }

    @FXML
    void newUserButtonClicked(ActionEvent event) {
        //Load new user pane
        AnchorPane newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("../resources/NewUser.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.getChildren().setAll(newRoot);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File directory = new File("./data/usernames");
        File[] dirfiles = directory.listFiles();
        ObservableList<User> users = FXCollections.observableArrayList();
        Scanner reader;

        if (dirfiles != null) {
            for (File file : dirfiles) {
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
