package namesayer.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.concurrent.Task;
import sun.audio.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

//Input of a string with destination of audio file to play - uses AudioStream and AudioPlayer for .wav files
public class PlayAudio {
    private String audio;
    private List<String> combineAudio = new ArrayList<>();
    private AudioStream audioStream;

    public PlayAudio(String audio) {
        this.audio = audio;
    }

    public PlayAudio(List<String> combineAudio, String audio) {
        this.combineAudio = combineAudio;
        this.audio = audio;
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

    public void filterCombinedAudio() {
        //Combine audio clips together
        FileWriter writer = null;
        try {
            writer = new FileWriter("temp/combineAudio.txt");
            for (String str : combineAudio) {
                writer.write("file '" + str + "CONCAT.wav'\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Silence noise sections of each audio
        for (String nameAudio : combineAudio) {
            String silenceAudio = "cp data/names/" + nameAudio + ".wav temp/" + nameAudio + ".wav\n" +
                    "ffmpeg -hide_banner -i temp/" + nameAudio + ".wav -af silenceremove=1:0:-35dB:1:5:-35dB:0 temp/" + nameAudio + "CONCAT.wav\n";
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", silenceAudio);
            try {
                processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void concatCombinedAudio() {
        //Concatenate from txt file
        String concatAudio = "ffmpeg -f concat -safe 0 -i temp/combineAudio.txt -c copy temp/" + audio.replace(" ", "") + " \n";
        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", concatAudio);
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopAudio() {
        AudioPlayer.player.stop(audioStream);
    }
}
