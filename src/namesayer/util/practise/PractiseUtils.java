package namesayer.util.practise;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import namesayer.util.HelpDialog;
import namesayer.util.Name;

import java.io.File;
import java.util.Collections;

/**
 * PractiseUtils deals with a lot of common logic in PractiseController
 * @author Eric Leung, Kevin Xu
 */
public class PractiseUtils {

    public PractiseUtils(){}

    /**
     * Checks to see if the name exists on the Listview in Practise menu
     */
    public boolean duplicateCheck(String name,
                               ObservableList<String> selectedNameList,
                               JFXListView<String> searchNamesView,
                               JFXTextField searchTextField,
                               StackPane stackPane) {
        //Show JFX dialog to warn user about duplicate play name
        if (selectedNameList.contains(name)) {
            String dupString = "Sorry! " + name + " already in your playlist!";
            HelpDialog helpDialog = new HelpDialog();
            helpDialog.showDuplicateDialog(stackPane, dupString);

            searchNamesView.setVisible(false);
            Platform.runLater(() -> searchTextField.clear());
            return true; // isDuplicate
        }

        return false; // notDuplicate
    }

    /**
     * populateList: Getting names of all name files in data/names and puts into all names list (sorted)
     */
    public void populateList(ObservableList<String> searchNameList) {
        File namesFolder = new File("./data/names");
        File[] listOfNames = namesFolder.listFiles();

        // iterate through list of audio files
        for (File file : listOfNames) {
            String fileName = file.getName();
            String[] parts = fileName.split("_");
            int extIndex = parts[3].indexOf(".");

            //Check if duplicate (or already in list)
            boolean dupFlag = false;
            Name nameObject = new Name(parts[3].substring(0, extIndex));
            for (String stringName : searchNameList) {
                if (nameObject.getName().equalsIgnoreCase(stringName)) {
                    dupFlag = true;
                }
            }
            //If not duplicate add to list
            if (!dupFlag) {
                String upperName = nameObject.getName().toLowerCase(); //Consistency with cases
                searchNameList.add(upperName);
            }
        }
        Collections.sort(searchNameList);
    }
}
