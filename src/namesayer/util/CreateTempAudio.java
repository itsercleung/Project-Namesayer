package namesayer.util;

import javafx.concurrent.Task;

import java.io.IOException;

public class CreateTempAudio {
    private String name;
    private Thread thread;
    private Task task;

    public CreateTempAudio(String name) {
        this.name = name;
    }

    //Execute ffmpeg recording which creates audio file in temp
    public void createSingleAudio() {
        String userAudio = "cd temp\n" +
                "ffmpeg -loglevel quiet -y -f alsa -i default -t 3 -ab 16 -ar 22050 -ac 1 " + name + ".wav";

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

    // TODO figure a way to stop recording halfway if the user decides.
    // kill thread? ffmpeg feature?
    // another way is to count the time between record and stop and cut out
    // the time

    public void stopRecording() {
        task.cancel(); //???
        thread.interrupt();// doesn't work
    }

}
