package namesayer.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.concurrent.Task;
import sun.audio.*;

//Input of a string with destination of audio file to play - uses AudioStream and AudioPlayer for .wav files
public class PlayAudio {
    private String audio;
    private List<String> combineAudio = new ArrayList<>();
    private AudioStream audioStream;

    public PlayAudio(String audio) {
        this.audio = audio;
    }

    public PlayAudio(List<String> combineAudio) {
        this.combineAudio = combineAudio;
    }

    public void playAudio() {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                InputStream in = null;
                try {
                    in = new FileInputStream(audio);
                    audioStream = new AudioStream(in);
                    AudioPlayer.player.start(audioStream); // play the audio clip with the audioplayer class
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread (task).start();
    }

    public void playCombinedAudio() {

    }

    public void stopAudio() {
        AudioPlayer.player.stop(audioStream);
    }
}
