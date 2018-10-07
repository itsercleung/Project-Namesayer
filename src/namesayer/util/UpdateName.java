package namesayer.util;

import javafx.concurrent.Task;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateName {
    //Initial filtering of given database, naming conventions and putting inside filteredNames folder
    public void updateNamesDB() {
        String filterNames = "cd data/names\n" +
                "for f in *.wav ; do mv -- \"$f\" \"$(tr [:upper:] [:lower:] <<< \"$f\")\" ; done\n";

        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", filterNames);
                try {
                    Process process = processBuilder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        new Thread (task).start();
    }

    //Whenever a new name is made from recording of user, then single filter such recording and place in filteredNames folder
    public void updateNewName(String name) {
        String update = "cd data/names\n" +
                "mv -- \"$f\" \"$(tr [:upper:] [:lower:] <<< \"$f\")\"";

        ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", update);
        try {
            Process updateProcess = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
