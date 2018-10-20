package namesayer;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import namesayer.util.FXMLResource;
import namesayer.util.FXMLResourceLoader;
import namesayer.util.HelpDialog;
import javafx.scene.text.Text;
import namesayer.login.User;
import namesayer.login.UserUtils;
import namesayer.util.Recorder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * TestMicrophoneController: Provides functionality to test microphone scene, where user may test their current microphone
 * setup to check and compare if their microphone is at an appropriate level during recording. Also includes being able to
 * record test audio and able to listen to their recording for additional comparison.
 */
public class TestMicrophoneController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private StackPane stackPane;
    @FXML private Button listenButton,testButton,testMicrophoneButton;
    @FXML private Button helpButton, rewardButton, exitButton;
    @FXML private MediaView mediaTest;
    @FXML private Label testLabel;
    @FXML private ProgressBar micLevel;
    @FXML private Text userText, pointsText;

    private Recorder recorder = null;
    private FXMLResourceLoader loader = new FXMLResourceLoader();

    //When user completes test, let them play back recording to hear if their mic is viable
    @FXML private void exitPressed(ActionEvent event) {
        recorder.close();
        StackPane loginRoot = null;
        loader.load(FXMLResource.LOGOUT, loginRoot, mainRoot);
    }

    //Load help popup
    @FXML private void helpPressed(ActionEvent event) {
        HelpDialog helpDialog = new HelpDialog(helpButton);
        helpDialog.showHelpDialog(stackPane,1);
    }

    //Load rewards window
    @FXML private void rewardPressed(ActionEvent event) {
        recorder.close();
        StackPane rewardsRoot = null;
        loader.load(FXMLResource.REWARD, rewardsRoot, mainRoot);
    }

    //Load practise window
    @FXML private void practisePressed(ActionEvent event) {
        recorder.close();
        StackPane practiseRoot = null;
        loader.load(FXMLResource.PRACTISE,practiseRoot,mainRoot);
    }

    //Load record name window
    @FXML private void recordNamePressed(ActionEvent event) {
        recorder.close();
        StackPane practiseRoot = null;
        loader.load(FXMLResource.RECORD_NEW,practiseRoot,mainRoot);
    }

    //When user completes test, let them play back recording to hear if their mic is viable 
    @FXML
    void listenPressed(ActionEvent event) {
        //When button is pressed disable button until audio is finished playing
        new Thread() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        listenButton.setDisable(true);
                        testButton.setDisable(true);
                    }
                });
                try {
                    Thread.sleep(3000); //For until test.wav finishes
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    public void run() {
                        listenButton.setDisable(false);
                        testButton.setDisable(false);

                        //Once steps (1)-(2) complete testlabel will show complete
                        testLabel.setText("[Test complete]");
                        testLabel.setStyle("-fx-text-fill: #6AE368"); //Nice spotify colour
                    }
                });
            }
        }.start();

        //Playing test.wav on media view
        File file = new File("./temp/test.wav");
        String source = file.toURI().toString();
        Media media = new Media(source);
        MediaPlayer player = new MediaPlayer(media);
        mediaTest.setMediaPlayer(player);
        player.play();
    }

    //Records users microphone and puts resultant recording into temp file for user to hear
    @FXML
    void testPressed(ActionEvent event) {
        //Set label to show test is currently ongoing
        testLabel.setText("[Testing]");
        testLabel.setStyle("-fx-text-fill: #FF5252");
        testButton.setDisable(true);

        //Create test.wav file with samplingRate variable to adjust (could possibly adjust for "loudness")
        String testAudio = "cd temp\n" +
                "ffmpeg -loglevel quiet -y -f alsa -i default -t 3 -ab 16 -ar 22050 -ac 1 test.wav";

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", testAudio);
                try {
                    Process process = processBuilder.start();
                    process.waitFor();
                    //Enable buttons once recording finished
                    listenButton.setDisable(false);
                    testButton.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initial default properties for buttons
        testMicrophoneButton.setDisable(true);
        listenButton.setDisable(true);

        //Initial mic properties
        micLevel.setProgress(0);
        recorder = new Recorder(micLevel);
        Thread thread = new Thread(recorder);
        thread.setDaemon(true); // hax but not graceful shutdown of thread
        thread.start();

        //Set user current name and score
        User user = UserUtils.getCurrentLoginUser(userText, pointsText);

        //Setting image icons to buttons
        //Testing navigation
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        listenButton.setGraphic(new ImageView(play));
        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        testButton.setGraphic(new ImageView(rec));
        testButton.setOnMouseEntered(e -> testButton.setGraphic(new ImageView(recHover)));
        testButton.setOnMouseExited(e -> testButton.setGraphic(new ImageView(rec)));

        // Reward and help Popup icons
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton);
        iconLoader.loadMenuIcons();
    }
}


