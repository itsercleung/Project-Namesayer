package namesayer.login;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    }

    @FXML
    void newUserButtonClicked(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Please enter a new username:");
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        TextField input = dialog.getEditor();
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> isInvalid(input.getText()), input.textProperty());
        okButton.disableProperty().bind(isInvalid);
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(username -> {
            if (username.equals("")) {
                return;
            }

            String path = "./data/usernames/"+username+".txt";
            File usernameTxt = new File(path);
            if(usernameTxt.exists()) {
                return;
            }

            // add username and points
            try {
                FileWriter fw = new FileWriter(usernameTxt,true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.append(username);
                bw.newLine();
                bw.append("0");
                bw.close();
            } catch (IOException ioe) {

            }
        });
    }

    private boolean isInvalid(String text) {
        if (text.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File directory = new File("./data/usernames");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File[] dirfiles = directory.listFiles();
        ObservableList<User> users = FXCollections.observableArrayList();

        if (dirfiles != null) {
            for (File file : dirfiles) {
                try {
                    Scanner scanner = new Scanner(file);
                    String username = scanner.next();
                    System.out.println(username);
                    int points = Integer.valueOf(scanner.next());
                    System.out.println(points);
                    users.add(new User(username,points));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            userList.setItems(users);
        }
    }
}
