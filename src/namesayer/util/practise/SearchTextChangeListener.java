package namesayer.util.practise;

import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;

public class SearchTextChangeListener implements ChangeListener<String> {

    private String concatName;
    private ObservableList<String> searchNameList;
    private FilteredList<String> filteredData;
    private JFXListView<String> searchNamesView;

    public SearchTextChangeListener(String concatName,
                                    ObservableList<String> searchNameList,
                                    FilteredList<String> filteredData,
                                    JFXListView<String> searchNamesView) {
        this.concatName = concatName;
        this.searchNameList = searchNameList;
        this.filteredData = filteredData;
        this.searchNamesView = searchNamesView;
    }

    /**
     * changeHeightView: Dynamically changes height of the listView (scales up to be similar to a search drop down)
     */
    private void changeHeightView() {
        double maxHeight = 180;
        double currentHeight = filteredData.size() * 30;
        if (currentHeight >= maxHeight) {
            searchNamesView.setMaxHeight(180);
        } else {
            searchNamesView.setMaxHeight(currentHeight);
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        //IF "-" or " " exists from user input and such words exist in database, get the concatenated version and display on search
        String[] dispConcat;
        int wordCount = 0;
        if (newValue.contains("-") || newValue.contains(" ")) {
            dispConcat = newValue.split("[-\\s+]"); // whitespace delimiter with hyphen
            for (String singleName : dispConcat) {
                for (String names : searchNameList) {
                    if (names.toLowerCase().equals(singleName.toLowerCase())) {
                        wordCount++;
                    }
                }
            }

            if (wordCount == dispConcat.length && wordCount > 1) {
                concatName = String.join(" ", dispConcat).toLowerCase();
                // get the filter list of names and add concat name to top of result list
                List list = filteredData.getSource(); // TESTING: this might be a bad idea?
                if (!list.contains("[COMBINE]: " + concatName)) {
                    list.add(0, "[COMBINE]: " + concatName);
                }
            }
        }

        //Handle single word names through user search
        filteredData.setPredicate(name -> {
            // If filter text is empty, display all persons.
            if (newValue.equals("") || newValue.isEmpty()) {
                searchNamesView.setVisible(false);
                return true;
            }

            // splitting string by "-" and/or " "
            String[] allNames;
            if (newValue.contains("-") || newValue.contains(" ")) {
                allNames = newValue.split("[-\\s+]"); // whitespace delimiter with hyphen
                for (String singleName : allNames) {
                    if (name.toLowerCase().contains(singleName.toLowerCase())) {
                        return true;
                    }
                }
            }

            // Compare first name and last name of every client with filter text - if filter matches first name then RETURN
            changeHeightView();
            String lowerCaseFilter = newValue.toLowerCase();
            searchNamesView.setVisible(true);
            return name.toLowerCase().contains(lowerCaseFilter);
        });
    }
}