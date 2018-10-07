package namesayer;

import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import namesayer.util.CreateTempAudio;
import namesayer.util.PlayAudio;
import namesayer.util.UpdateName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class RecordNewController implements Initializable {

    @FXML private Button listenButton, recordButton, saveButton, recordNameButton;
    @FXML private AnchorPane root;
    @FXML private JFXTextField nameField;
    @FXML private Label label;
    @FXML private Button stopRecordingButton;
    @FXML private VBox vbox;

    private String name;
    private String officialName;
    private CreateTempAudio createTempAudio;
    private JFXSnackbar message;

    @FXML
    private void testMicrophonePressed(ActionEvent event) {
        //Load testMicrophone pane
        AnchorPane testMicrophoneRoot = null;
        try {
            testMicrophoneRoot = FXMLLoader.load(getClass().getResource("resources/TestMicrophone.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        root.getChildren().setAll(testMicrophoneRoot);
    }

    @FXML
    private void practisePressed(ActionEvent event) {
        //Load practise pane
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/Practise.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.getChildren().setAll(practiseRoot);
    }

    @FXML
    private void recordNamePressed(ActionEvent event) {
        //record new practise pane
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/RecordNew.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        root.getChildren().setAll(practiseRoot);
    }

    @FXML
    void exitPressed(ActionEvent event) {
        System.exit(0);
    }

    //Allows user to record given the string of the nameField: (1) If name already exists, assign the name with version
    //(2) Else simply record the name
    @FXML
    private void recordPressed(ActionEvent event) {
        String regex = "([a-zA-Z0-9])*"; // letters, numbers
        name = nameField.getText().trim().toLowerCase();
        if (!name.matches(regex) || name.isEmpty()) {
            String error = "[Invalid Name: Name must have letters or numbers only]";
            if (message != null) {message.close();}
            message.show(error,10000);
            //label.setText(error);

            return;
        }

        // patching playback issue
        if (name.contains(" ")) {
            name = name.replace(" ","_");
        }

        //Setup official name for saved recording
        DateFormat dateFormat = new SimpleDateFormat("_dd-MM-yyyy_HH-mm-ss_");
        Date date = new Date();
        officialName = "user" + dateFormat.format(date) + name;

        // get number of versions of name
        int version = 0;
        String command = "cd data/names; ls | grep _" + name + ".wav | wc -l";
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
        try {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            version = Integer.valueOf(line)+1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If name exists in data ask user if they want to make another existing recording with same name. If yes then
        //simply start recording for (3 SECONDS)
        if (version > 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    name+" already exists.\n" +
                            "Do you want to record another version\n" +
                            "of "+name+"?");

            alert.showAndWait().ifPresent(response -> {
               if (response == ButtonType.CANCEL) {
                   return;
               }
               else if (response == ButtonType.OK) {
                   String messageString = "[Recording " + name + "]";
                   message.close();
                   message.show(messageString, 10000);
                   //label.setText(messageString);
                   new Thread() {
                       public void run() {
                           Platform.runLater(new Runnable() {
                               public void run() {
                                   disableButtons();
                               }
                           });
                           try {
                               Thread.sleep(3000); //For until test.wav finishes
                           }
                           catch(InterruptedException e) {
                               e.printStackTrace();
                           }
                           Platform.runLater(new Runnable() {
                               public void run() {
                                   String messageString = "[Recorded " + name + "]";
                                   message.close();
                                   message.show(messageString, 10000);
                                   //label.setText(messageString);
                                   enableButtons();
                               }
                           });
                       }
                   }.start();
                   createTempAudio = new CreateTempAudio(officialName);
                   createTempAudio.createSingleAudio();
               }
            });
        }
        //Else if name doesn't exist, simply start recording (3 SECONDS)
        else {
            new Thread() {
                public void run() {
                    Platform.runLater(new Runnable() {
                        public void run() {
                            String messageString = "[Recording " + name + "]";
                            message.close();
                            message.show(messageString, 10000);
                            //label.setText(messageString);
                            disableButtons();
                        }
                    });
                    try {
                        Thread.sleep(3000); //For until test.wav finishes
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        public void run() {
                            String messageString = "[Recorded " + name + "]";
                            message.close();
                            message.show(messageString, 10000);
                            //label.setText(messageString);
                            enableButtons();
                        }
                    });
                }
            }.start();
            createTempAudio = new CreateTempAudio(officialName);
            createTempAudio.createSingleAudio();
        }
    }

    @FXML
    private void stopRecordingButtonPressed() {
        createTempAudio.stopRecording();
    }

    //Listen to temp recording to see if user wants to save
    @FXML
    private void listenPressed(ActionEvent event) {
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        String messageString = "[Listening to " + name + "]";
                        message.close();
                        message.show(messageString, 10000);
                        //label.setText(messageString);
                        disableButtons();
                        stopRecordingButton.setDisable(true);
                    }
                });
                try {
                    Thread.sleep(3000); //For until test.wav finishes
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        String messageString = "[Listened to " + name + "]";
                        message.close();
                        message.show(messageString, 10000);
                        //label.setText(messageString);
                        enableButtons();
                    }
                });
            }
        }.start();

        String path = "./temp/" + officialName + ".wav";
        PlayAudio playAudio = new PlayAudio(path);
        playAudio.playAudio();
    }

    //Once saved, place into unfilteredNames folder
    @FXML
    void savePressed(ActionEvent event) {
        String messageString = "[Saved name as " + name + "]";
        message.close();
        message.show(messageString, 10000);
        //label.setText(messageString);
        try {
            Files.move(Paths.get("./temp/" + officialName + ".wav"),
                    Paths.get("data/names/" + officialName + ".wav"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Update practice list
        UpdateName updateName = new UpdateName();
        updateName.updateNewName(officialName);

        //Reset to default button layout and fields
        saveButton.setDisable(true);
        listenButton.setDisable(true);
        nameField.clear();
    }

    private void disableButtons() {
        recordButton.setDisable(true);
        listenButton.setDisable(true);
        saveButton.setDisable(true);
        nameField.setDisable(true);
        stopRecordingButton.setDisable(false); // stop recording is exception
    }

    private void enableButtons() {
        recordButton.setDisable(false);
        listenButton.setDisable(false);
        saveButton.setDisable(false);
        nameField.setDisable(false);
        stopRecordingButton.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recordNameButton.setDisable(true);
        listenButton.setDisable(true);
        saveButton.setDisable(true);
        stopRecordingButton.setDisable(true);

        //Set icons to specific buttons from resources/icons (credited in description).
        //Set icons for record new menu
        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordButton.setGraphic(new ImageView(rec));
        recordButton.setOnMouseEntered(e -> recordButton.setGraphic(new ImageView(recHover)));
        recordButton.setOnMouseExited(e -> recordButton.setGraphic(new ImageView(rec)));
        Image save = new Image(getClass().getResourceAsStream("resources/icons/saveHover.png"));
        saveButton.setGraphic(new ImageView(save));
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        listenButton.setGraphic(new ImageView(play));

        message = new JFXSnackbar(vbox);
        message.setStyle("-fx-font-size: 20pt;");
    }
}
