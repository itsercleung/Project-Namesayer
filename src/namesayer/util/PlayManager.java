package namesayer.util;

import javafx.application.Platform;
import javafx.scene.control.Button;
import namesayer.PractiseController;

/**
 * PlayManager deals with timing, threading and button logic when playing
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
     * playOldAudio plays the currently selected audio
     * in the list in PractiseController.
     * @param practiseController
     * @param currentNameNum
     */
    public void playOldAudio(PractiseController practiseController, int currentNameNum) {
        playPlatform(practiseController,currentNameNum);
        setDelay();
        Platform.runLater(this::disableButtons);
    }

    /**
     * Used to play the old audio in playOldAudio and playAlternateAudio
     * @param practiseController
     * @param currentNameNum
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
     * Arbitrary delay to try and prevent RIFF errors
     */
    public void setDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void enableButtons() {
        playButton.setDisable(true);
        recordButton.setDisable(true);
        stopButton.setDisable(false);
    }

    public void disableButtons() {
        playButton.setDisable(false);
        recordButton.setDisable(false);
        stopButton.setDisable(true);
    }

    /**
     * Plays the currently recorded audio.
     * @param tempAudioName
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
     * Alternates once between playOld and playNew audios.
     * @param practiseController
     * @param currentNameNum
     * @param tempAudioName
     */
    public void playAlternateAudio(PractiseController practiseController, int currentNameNum, String tempAudioName) {
        playPlatform(practiseController,currentNameNum);
        setDelay();
        playNewAudio(tempAudioName);
        setDelay();
    }
}
