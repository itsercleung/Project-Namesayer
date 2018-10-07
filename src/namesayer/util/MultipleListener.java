package namesayer.util;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.controlsfx.control.CheckListView;

import java.util.List;

public class MultipleListener implements ListChangeListener {

    @FXML
    private ListView playListView;
    @FXML
    private CheckListView checkListView;
    @FXML
    private Button playNames;

    private List<String> playList;

    public MultipleListener(ListView playListView, CheckListView checkListView, Button playNames, List playList) {
        this.playListView=playListView;
        this.checkListView=checkListView;
        this.playNames=playNames;
        this.playList=playList;
    }

    @Override
    public void onChanged(Change c) {
        playList.clear();
        playList.addAll(checkListView.getCheckModel().getCheckedItems());

        playListView.getItems().clear();
        playListView.getItems().addAll(playList);

        //Enables/Disables play button only if user has selected name(s)
        if (!playList.isEmpty()) {
            playNames.setDisable(false);
        }
        else {
            playNames.setDisable(true);
        }
    }
}
