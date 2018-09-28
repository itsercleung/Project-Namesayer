package namesayer.util;

import javafx.concurrent.Task;

import java.io.IOException;

public class CreateTempAudio {
    private String name;

    public CreateTempAudio(String name) {
        this.name = name;
    }

    //Execute ffmpeg recording which creates audio file in temp
    public void createSingleAudio() {
        String userAudio = "cd temp\n" +
                "ffmpeg -loglevel quiet -y -f alsa -i default -t 3 -ab 16 -ar 22050 -ac 1 " + name + ".wav";

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
        new Thread (task).start();
    }

}
