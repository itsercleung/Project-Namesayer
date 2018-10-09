package namesayer.login;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewUserController implements Initializable {

    @FXML private JFXTextField usernameField;
    @FXML private Button cancelButton;
    @FXML private Button registerButton;
    @FXML private AnchorPane root;

    @FXML
    void registerButtonPressed(ActionEvent event) {
        UserUtils.createUser(usernameField.getText());
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancelButtonPressed(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // disable button if field is empty
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> usernameField.getText().trim().isEmpty(), usernameField.textProperty());
        registerButton.disableProperty().bind(isInvalid);
    }
}
