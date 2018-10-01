package namesayer.login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewUserController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane root;

    @FXML
    void registerButtonPressed(ActionEvent event) {
        String path = "./data/usernames/" + usernameField.getText() + ".txt";
        File usernameTxt = new File(path);
        if (usernameTxt.exists()) {
            // give warning and not exit
            return;
        }

        // add username and points
        try {
            FileWriter fw = new FileWriter(usernameTxt, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(usernameField.getText());
            bw.newLine();
            bw.append("0");
            bw.close();
        } catch (IOException ioe) {

        }

        //Load practise pane
        AnchorPane newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("../resources/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.getChildren().setAll(newRoot);
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        //Load practise pane
        AnchorPane newRoot = null;
        try {
            newRoot = FXMLLoader.load(getClass().getResource("../resources/Login.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.getChildren().setAll(newRoot);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // disable button if field is empty
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> usernameField.getText().trim().isEmpty(), usernameField.textProperty());
        registerButton.disableProperty().bind(isInvalid);
    }
}
