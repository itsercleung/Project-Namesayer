package namesayer;

import javafx.fxml.Initializable;
import namesayer.util.IconLoader;

import java.io.File;
import java.io.IOException;

/**
 * MainController: Initializes functionality of the main sidebar menu and other components. Main menu doesn't have any
 * functionality other than to welcome the user and provide sidemenu to navigate to other controllers
 */
public class MainController extends NameSayerMenuController implements Initializable {

    //Method to delete all existing temp files within the folder (usually deleted on startup)
    public void deleteTemp() {
        File temp = new File("temp/");
        if (temp.exists() && temp.isDirectory()) {
            String[] entries = temp.list();
            for(String s: entries){
                File currentFile = new File(temp.getPath(),s);
                currentFile.delete();
            }
        }
    }

    public void init() {
        deleteTemp(); //On startup delete temp files in folder

        //If txt doesnt exist then make one and append TITLE
        File pqFile = new File("data/ratingAudio.txt");
        if (!pqFile.exists()) {
            try {
                pqFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Creating temp folder and data folder
        new File("./temp").mkdirs();
        new File("./data").mkdirs();
        new File("./data/names").mkdirs();
        new File("./data/usernames").mkdirs();

        // Reward and help Popup icons
        IconLoader iconLoader = new IconLoader(user,rewardButton,helpButton,exitButton);
        iconLoader.loadMenuIcons();
    }
}
