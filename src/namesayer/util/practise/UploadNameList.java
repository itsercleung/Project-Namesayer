package namesayer.util.practise;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import namesayer.util.HelpDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * UploadNameList deals with the data related to uploading a txt file.
 * @author Eric Leung, Kevin Xu
 */
public class UploadNameList {

    private ObservableList<String> selectedNameList, searchNameList;
    private JFXListView<String> searchNamesView,selectedNamesView;
    private StackPane stackPane;
    private JFXTextField searchTextField;
    private AnchorPane mainRoot;
    private PractiseUtils practiseUtils = new PractiseUtils();

    public UploadNameList(ObservableList<String> selectedNameList,
                          ObservableList<String> searchNameList,
                          JFXListView<String> searchNamesView,
                          JFXListView<String> selectedNamesView,
                          StackPane stackPane,
                          JFXTextField searchTextField,
                          AnchorPane mainRoot) {
        this.selectedNameList = selectedNameList;
        this.searchNameList = searchNameList;
        this.searchNamesView = searchNamesView;
        this.stackPane = stackPane;
        this.selectedNamesView = selectedNamesView;
        this.searchTextField = searchTextField;
        this.mainRoot = mainRoot;
    }

    /**
     * Sets up the upload txt file menu, and checks if the
     * file is a valid txt file and checks if names
     * on the txt file are valid.
     * @param uploadButton
     */
    public void upload(Button uploadButton) {
        // let the user select TXT files to upload
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        fc.setTitle("Select Text (.txt) File");
        fc.getExtensionFilters().add(filter);
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File textFile = fc.showOpenDialog(stage); // should be txt file

        //If user cancels, return gracefully
        if (textFile == null) {
            return;
        }
        Scanner reader = null;
        try {
            reader = new Scanner(textFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // go through text file and check if name is in names db
        List<String> rejectList = new ArrayList<>();
        boolean flagCharLimit = false; //Indicate if over 50 chars name exists
        while (reader.hasNextLine()) {
            String name = reader.nextLine().replace("-", " ").toLowerCase();

            if (!selectedNameList.contains(name)) {
                if (name.length() <= 50) {
                    if (searchNameList.contains(name)) {
                        selectedNameList.add(name);
                    } else if (name.contains(" ") || name.contains("-")) {
                        String[] names = name.split("[-\\s+]"); // whitespace delimiter with hyphen
                        boolean canConcat = true;

                        // go through list of names, if it is a name that exists in database, then add it
                        for (String singleName : names) {
                            if (!searchNameList.contains(singleName.toLowerCase())) {
                                canConcat = false;
                                rejectList.add(name);
                                break;
                            }
                        }
                        if (canConcat) {
                            if (selectedNameList.contains("[COMBINE]: " + name)) {
                                practiseUtils.duplicateCheck("[COMBINE]: " + name,
                                        selectedNameList,
                                        searchNamesView,
                                        searchTextField,
                                        stackPane);

                            } else if (!selectedNameList.contains("[COMBINE]: " + name)) {
                                selectedNameList.add("[COMBINE]: " + name);
                            }
                        }
                    } else {
                        // add name to reject list (if name doesn't exist)
                        if (!name.isEmpty()) {
                            rejectList.add(name);
                        }
                    }
                }
                else if (name.length() > 50 && !flagCharLimit) {
                    flagCharLimit = true;
                    HelpDialog helpDialog = new HelpDialog();
                    helpDialog.showLongNameDialog(stackPane);
                }
            } else if (selectedNameList.contains(name)) {
                practiseUtils.duplicateCheck(name,
                        selectedNameList,
                        searchNamesView,
                        searchTextField,
                        stackPane);
            }
        }

        //Display helpdialog if user inputs duplicate name
        if (rejectList.size() > 0) {
            String label = "The following names are not available:\n\n";

            for (String name : rejectList) {
                StringBuilder sb = new StringBuilder(label);
                label = sb.append("-" + name + "\n").toString();
            }

            HelpDialog helpDialog = new HelpDialog();
            helpDialog.showDuplicateDialog(stackPane, label);
        }
        selectedNamesView.setItems(selectedNameList);
    }
}
