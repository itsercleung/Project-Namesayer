package namesayer.reward;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import namesayer.login.User;

import java.io.IOException;

/**
 * concepts copied from UserCell
 */
public class RewardCell extends ListCell<Reward> {
    @FXML private AnchorPane root;
    @FXML private Text nameText, descriptionText;
    @FXML private ImageView image;

    private FXMLLoader loader;
    private User user;

    public RewardCell(User user) {
        super();
        this.user = user;
    }

    @Override
    protected void updateItem(Reward reward, boolean empty) {
        super.updateItem(reward, empty);
        setText(null);
        setGraphic(null);

        if (reward != null) {
            try {
                loader = new FXMLLoader(getClass().getResource("../reward/RewardCell.fxml"));
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nameText.setText(reward.getRewardName());
            descriptionText.setText(reward.getRewardDescription());
            image.setImage(new Image(getClass().getResourceAsStream(reward.getImageURL())));
            // set graphic here
            setGraphic(root);

            if (reward.getMinPoints() > user.getPoints()) {
                setDisabled(true);
            }
        }


    }
}
