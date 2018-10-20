package namesayer.util;

import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * CreateAudio: Creates temporary audio files given the name. Can be used for making test audio files, or
 * initial audio files that could be saved depending on the users choice.
 */
public class CreateAudio {
    private String name;

    public CreateAudio(String name) {
        this.name = name;
    }

    /**
     *  Execute ffmpeg recording which creates audio file in temp
     */
    public void createSingleAudio() {
        //Create audio in linux ffmpeg command
        String userAudio = "cd temp\n" +
                "ffmpeg -loglevel quiet -y -f alsa -i default -t 4.5 -ar 22050 -ac 1 " + name.replace(" ","") + ".wav";

        Task task = new Task<Void>() {
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
        Thread thread = new Thread(task);
        thread.start();
    }

    /**
     * Saves audio created in createSingleAudio method into data folder
     */
    public void saveAudio() {
        try {
            Files.move(Paths.get("temp/" + name.replace(" ","") + ".wav"),
                    Paths.get("./data/names/" + name.replace(" ","") + ".wav"),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
