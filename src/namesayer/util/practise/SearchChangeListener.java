package namesayer.util.practise;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import namesayer.util.HelpDialog;

/**
 * SearchChangeListener is a ChangeListener that detects and populates
 * a list when the user types in a name to search.
 */
public class SearchChangeListener implements ChangeListener<String> {

    private ObservableList<String> selectedNameList, searchNameList;
    private JFXListView<String> searchNamesView, selectedNamesView;
    private StackPane stackPane;
    private JFXTextField searchTextField;
    private AnchorPane mainRoot;

    private PractiseUtils practiseUtils = new PractiseUtils();

    public SearchChangeListener(ObservableList<String> selectedNameList,
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

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String selectedName = searchNamesView.getSelectionModel().getSelectedItem();
        //Checking if not empty and not more than 50 characters
        if (selectedName != null && selectedName.length() <= 50) {
            if (!selectedNameList.contains(selectedName)) {
                practiseUtils.duplicateCheck(selectedName,
                        selectedNameList,
                        searchNamesView,
                        searchTextField,
                        stackPane);
                selectedNameList.add(selectedName);
                selectedNamesView.setItems(selectedNameList);
                searchNamesView.setVisible(false);

                //Clear textfield in main thread
                Platform.runLater(() -> {
                    searchTextField.clear();
                    searchNameList.clear();
                    practiseUtils.populateList(searchNameList);
                    mainRoot.requestFocus();
                });
            } else if (selectedNameList.contains(selectedName)) {
                practiseUtils.duplicateCheck(selectedName,
                        selectedNameList,
                        searchNamesView,
                        searchTextField,
                        stackPane);
            }
        } else if (selectedName != null && selectedName.length() > 50) {
            HelpDialog helpDialog = new HelpDialog();
            helpDialog.showLongNameDialog(stackPane);

            //Clear textfield in main thread
            Platform.runLater(() -> {
                searchTextField.clear();
                searchNameList.clear();
                mainRoot.requestFocus();
            });

        }
    }
}
