package namesayer.util;

import java.io.IOException;

/**
 * UpdateName: Allows for names to be moved from database if appropriate destination is given
 */
public class UpdateName {
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
