package namesayer.util;

import javafx.application.Platform;
import javafx.scene.control.Button;
import namesayer.PractiseController;

/**
 * PlayManager: deals with timing, threading and button logic when playing
 * audio in Play menu.
 */
public class PlayManager {

    private Button playButton, recordButton, stopButton;
    private PlayAudio playAudio;

    public PlayManager(Button playButton, Button recordButton, Button stopButton) {
        this.playButton = playButton;
        this.recordButton = recordButton;
        this.stopButton = stopButton;
    }

    /**
     * playOldAudio: plays the currently selected audio
     * in the list in PractiseController.
     * @param practiseController : Practice controller object to assign platform
     * @param currentNameNum : current selected number to play
     */
    public void playOldAudio(PractiseController practiseController, int currentNameNum) {
        playPlatform(practiseController,currentNameNum);
        setDelay();
        Platform.runLater(this::disableButtons);
    }

    /**
     * playPlatform: Used to play the old audio in playOldAudio and playAlternateAudio
     * @param practiseController : Practice controller object to assign platform
     * @param currentNameNum : current selected number to play
     */
    private void playPlatform(PractiseController practiseController, int currentNameNum) {
        Platform.runLater(() -> {
            enableButtons();
            String nameAudio = practiseController.getNamePlaylist().get(currentNameNum).toString() + ".wav";
            //If current name isn't combination
            if (!nameAudio.contains(" ")) {
                playAudio = new PlayAudio("./data/names/" + nameAudio);
                playAudio.playAudio();
            }
            //Else if name is combination - section names into appropriate format
            else {
                playAudio = new PlayAudio("./temp/" + nameAudio.replace(" ", ""));
                playAudio.playAudio();
            }
        });
    }

    /**
     * setDelay: Arbitrary delay to try and prevent RIFF errors
     */
    private void setDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enableButtons() {
        playButton.setDisable(true);
        recordButton.setDisable(true);
        stopButton.setDisable(false);
    }

    private void disableButtons() {
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    /**
     * playNewAudio: Plays the currently recorded audio.
     * @param tempAudioName : name of temporary created audio
     */
    public void playNewAudio(String tempAudioName) {
        Platform.runLater(() -> {
            enableButtons();
            String path = "./temp/" + tempAudioName.replace(" ", "") + ".wav";
            playAudio = new PlayAudio(path);
            playAudio.playAudio();
        });
        setDelay();
        Platform.runLater(this::disableButtons);
    }

    public void stopAudio() {
        playAudio.stopAudio();
    }

    /**
     * playAlternateAudio: Alternates once between playOld and playNew audios.
     * @param practiseController : given practise controller
     * @param currentNameNum : current name selected
     * @param tempAudioName : current temp audio name created
     */
    public void playAlternateAudio(PractiseController practiseController, int currentNameNum, String tempAudioName) {
        playPlatform(practiseController,currentNameNum);
        setDelay();
        playNewAudio(tempAudioName);
        setDelay();
    }
}
