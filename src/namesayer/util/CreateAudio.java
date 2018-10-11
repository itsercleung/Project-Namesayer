package namesayer.util;

import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * CREATETEMPAUDIO Class: Creates temporary audio files given the name. Can be used for making test audio files, or
 * initial audio files that could be saved depending on the users choice.
 */
public class CreateAudio {
    private String name;
    private Thread thread;
    private Task task;

    public CreateAudio(String name) {
        this.name = name;
    }

    /**
     *  Execute ffmpeg recording which creates audio file in temp
     */
    public void createSingleAudio() {
        String userAudio = "cd temp\n" +
                //"ffmpeg -f alsa -i hw:0 -t 3 -acodec pcm_s16le -ar 22050 -ac 1 " + name.replace(" ","") + ".wav";
                // TODO i think the above code is the source of the problem
                "ffmpeg -loglevel quiet -y -f alsa -i default -t 5 -ar 22050 -ac 1 " + name.replace(" ","") + ".wav";

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", userAudio);
                try {
                    Process process = processBuilder.start();
                    process.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        thread = new Thread (task);
        thread.start();
    }

    /**
     * Saves audio created in createSingleAudio method into data folder
     */
    public void saveAudio() {
        try {
            Files.move(Paths.get("./temp/" + name.replace(" ","") + ".wav"),
                    Paths.get("./data/names/" + name.replace(" ","") + ".wav"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // kill thread? ffmpeg feature?
    // another way is to count the time between record and stop and cut out
    // the time
    // too complicated to implement, so design decision to stuff it
    public void stopRecording() {
        task.cancel(); //???
        thread.interrupt();// doesn't work
    }

}