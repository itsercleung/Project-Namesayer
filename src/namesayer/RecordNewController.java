package namesayer;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import namesayer.login.Points;
import namesayer.login.UserUtils;
import namesayer.util.CreateAudio;
import namesayer.util.IconLoader;
import namesayer.util.PlayAudio;
import namesayer.util.UpdateName;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * RecordNewController: For dealing with functionality of record new scene, where users can record new names if they need to.
 * This includes recording name for 4 seconds, and providing options to listen to recording, stop listening of recording and
 * saving recording to database.
 * This is included in case a new person is needed to be added into database easily.
 */
public class RecordNewController extends NameSayerMenuController implements Initializable {

    @FXML private Button listenButton, recordButton, saveButton, recordNameButton;
    @FXML private StackPane stackPane;
    @FXML private JFXTextField nameField;
    @FXML private Button stopPlayButton;
    @FXML private VBox vbox;

    private String name;
    private String officialName;
    private CreateAudio createAudio;
    private JFXSnackbar message;
    private PlayAudio playAudio = null;

    private static int RECORD_TOTAL_TIME = 5000; // to prevent RIFF exception

    //Allows user to record given the string of the nameField:
    //(1) If name already exists, assign the name with version
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
        officialName = user.getUsername().trim() + dateFormat.format(date) + name;

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
            //Creating dialog box to show with options if duplicate name
            Button okButton = new Button("OK");
            Button cancelButton = new Button("CANCEL");
            okButton.getStylesheets().add("namesayer/resources/stylesheet/general.css");
            cancelButton.getStylesheets().add("namesayer/resources/stylesheet/general.css");
            HBox buttonBox = new HBox();
            buttonBox.getChildren().addAll(okButton,cancelButton);
            buttonBox.setSpacing(5);

            //Setting to JFXDialog
            JFXDialogLayout dialogLayout = new JFXDialogLayout();
            JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
            dialogLayout.setHeading(new Label("Note"));
            dialogLayout.setBody(new Label("Already exists! Do you still want to add?"));
            dialogLayout.setActions(buttonBox);
            dialog.show();

            //Giving user option to save duplicate or not
            cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
                listenButton.setDisable(true);
                saveButton.setDisable(true);
                dialog.close();
            });

            //Continue with recording
            okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent )-> {
                dialog.close();
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
                            Thread.sleep(RECORD_TOTAL_TIME); //For until test.wav finishes
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(new Runnable() {
                            public void run() {
                                String messageString = "[Recorded " + name + "]";
                                message.close();
                                message.show(messageString, 10000);
                                enableButtons();
                            }
                        });
                    }
                }.start();
                createAudio = new CreateAudio(officialName);
                createAudio.createSingleAudio();
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
                        Thread.sleep(RECORD_TOTAL_TIME); //For until test.wav finishes
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(new Runnable() {
                        public void run() {
                            String messageString = "[Recorded " + name + "]";
                            message.close();
                            message.show(messageString, 10000);
                            enableButtons();
                        }
                    });
                }
            }.start();
            createAudio = new CreateAudio(officialName);
            createAudio.createSingleAudio();
        }
    }

    @FXML
    private void stopPlayButtonPressed() {
        playAudio.stopAudio();
        enableButtons();
        stopPlayButton.setDisable(false);
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
                        disableButtons();
                        stopPlayButton.setDisable(false);
                    }
                });
                try {
                    Thread.sleep(5000); //For until test.wav finishes
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        String messageString = "[Listened to " + name + "]";
                        message.close();
                        message.show(messageString, 10000);
                        enableButtons();
                        stopPlayButton.setDisable(true);
                    }
                });
            }
        }.start();

        String path = "temp/" + officialName + ".wav";
        playAudio = new PlayAudio(path);
        playAudio.playAudio();
    }

    //Once saved, place into unfilteredNames folder
    @FXML
    void savePressed(ActionEvent event) {
        File file = new File("./data/names/" + officialName + ".wav");
        if (!file.exists()) {
            String messageString = "[Saved name as " + name + "]";
            message.close();
            message.show(messageString, 10000);
            //label.setText(messageString);

            createAudio.saveAudio();

            //Update practice list
            UpdateName updateName = new UpdateName();
            updateName.updateNewName(officialName);

            //Reset to default button layout and fields
            saveButton.setDisable(true);
            listenButton.setDisable(true);
            nameField.clear();

            // add points
            UserUtils.updateUser(user, Points.CREATE_NAME, userText, pointsText);
        }
    }

    /**
     * disableButtons: disables correct buttons when recording
     */
    private void disableButtons() {
        recordButton.setDisable(true);
        listenButton.setDisable(true);
        saveButton.setDisable(true);
        nameField.setDisable(true);
    }

    /**
     * enableButtons: enables correct buttons after recording
     */
    private void enableButtons() {
        recordButton.setDisable(false);
        listenButton.setDisable(false);
        saveButton.setDisable(false);
        nameField.setDisable(false);
    }

    public void init() {
        //Initialize correct button setup
        recordNameButton.setDisable(true);
        listenButton.setDisable(true);
        saveButton.setDisable(true);
        stopPlayButton.setDisable(true);

        //Styling message box
        message = new JFXSnackbar(vbox);
        message.setStyle("-fx-font-size: 15px;");

        // Reward and help Popup icons
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton,
                recordButton,  saveButton, listenButton,  stopPlayButton);
        iconLoader.loadMenuIcons();
        iconLoader.loadRecordNewIcons();
    }

}
