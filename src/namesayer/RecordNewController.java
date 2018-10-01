package namesayer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
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
    @FXML private TextField nameField;
    @FXML private Label label;
    @FXML private Button stopRecordingButton;

    private String name;
    private String officialName;
    private PlayAudio playAudio;

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
        String regex = "([ a-zA-Z0-9-])*"; // letters, spaces, numbers hyphens
        name = nameField.getText().trim().toLowerCase();
        if (!name.matches(regex) || name.isEmpty()) {
            label.setText("[Invalid Name: Name must have letters, spaces, numbers and/or hyphens]");
            return;
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
                   label.setText("[Recording " + name + "]");

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
                                   label.setText("[Recorded " + name + "]");
                                   enableButtons();
                               }
                           });
                       }
                   }.start();
                   CreateTempAudio createTempAudio = new CreateTempAudio(officialName);
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
                            label.setText("[Recording " + name + "]");
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
                            label.setText("[Recorded " + name + "]");
                            enableButtons();
                        }
                    });
                }
            }.start();
            CreateTempAudio createTempAudio = new CreateTempAudio(officialName);
            createTempAudio.createSingleAudio();
        }
    }

    @FXML
    private void stopRecordingButtonPressed() {
        playAudio.stopAudio();
    }

    //Listen to temp recording to see if user wants to save
    @FXML
    private void listenPressed(ActionEvent event) {
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        label.setText("[Listening to " + name + "]");
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
                        label.setText("[Listened to " + name + "]");
                        enableButtons();
                    }
                });
            }
        }.start();

        String path = "./temp/" + officialName + ".wav";
        playAudio = new PlayAudio(path);
        playAudio.playAudio();
    }

    //Once saved, place into unfilteredNames folder
    @FXML
    void savePressed(ActionEvent event) {
        label.setText("[Saved name as " + name + "]");
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
    }

    private void enableButtons() {
        recordButton.setDisable(false);
        listenButton.setDisable(false);
        saveButton.setDisable(false);
        nameField.setDisable(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //recordNameButton.setDisable(true);
        listenButton.setDisable(true);
        saveButton.setDisable(true);
        stopRecordingButton.setDisable(true);

        // bind record name button so if text field is empty, disable button
        BooleanBinding isInvalid = Bindings.createBooleanBinding(() -> nameField.getText().trim().isEmpty(), nameField.textProperty());
        recordButton.disableProperty().bind(isInvalid);
    }
}
