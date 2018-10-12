package namesayer.util;

import javafx.application.Platform;
import javafx.scene.control.Button;
import namesayer.PlayController;
import namesayer.PractiseController;

public class PlayManager {

    private Button playButton, recordButton, stopButton;
    private PlayAudio playAudio;

    public PlayManager(Button playButton, Button recordButton, Button stopButton) {
        this.playButton = playButton;
        this.recordButton = recordButton;
        this.stopButton = stopButton;
    }

    private void playOldAudio(PractiseController practiseController, int currentNameNum) {
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
    }

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

    public void playNewAudio(String tempAudioName) {
        String path = "./temp/" + tempAudioName.replace(" ", "") + ".wav";
        PlayAudio playAudio = new PlayAudio(path);
        playAudio.playAudio();
    }

}
