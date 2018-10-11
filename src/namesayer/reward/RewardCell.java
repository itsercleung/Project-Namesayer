package namesayer.reward;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * concepts copied from UserCell
 */
public class RewardCell extends ListCell<Reward> {

    private FXMLLoader loader;

    public RewardCell() {
        super();
    }

    @Override
    protected void updateItem(Reward user, boolean empty) {
        super.updateItem(user, empty);
        setText(null);
        setGraphic(null);

        if (user != null) {
            try {
                loader = new FXMLLoader(getClass().getResource("../login/RewardCell.fxml"));
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // set graphic here
            // setGraphic();
        }


    }
}
