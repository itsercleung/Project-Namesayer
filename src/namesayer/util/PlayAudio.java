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
        audio = "combine.wav";

        //Combine audio clips together
        FileWriter writer = null;
        try {
            writer = new FileWriter("temp/combineAudio.txt");
            for (String str : combineAudio) {
                writer.write("file '" + str + "s.wav'\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playAudio() {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                InputStream in = null;
                try {
                    in = new FileInputStream(audio);
                    audioStream = new AudioStream(in);
                    AudioPlayer.player.start(audioStream); // play the audio clip with the audio player class
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public void playCombinedAudio() {
        //Silence noise sections of each audio
        for (String audio : combineAudio) {
            String silenceAudio = "cd data/names\n" +
                    "cp " + audio + ".wav ../../temp/" + audio + ".wav\n" +
                    "ffmpeg -y -hide_banner -i ../../temp/" + audio + ".wav -af silenceremove=1:0:-50dB ../../temp/" + audio + "s.wav\n" +
                    "cd temp/\n" +
                    "ffmpeg -f concat -safe 0 -i combineAudio.txt -c copy combine.wav\n";
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", silenceAudio);
            try {
                processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void stopAudio() {
        AudioPlayer.player.stop(audioStream);
    }
}
