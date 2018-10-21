package namesayer.reward;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import namesayer.login.User;

import java.io.IOException;

/**
 * RewardCell: Similarly concepts copied from UserCell. Creates cell and stores information of each reward to be displayed
 * in RewardController
 */
public class RewardCell extends ListCell<Reward> {
    @FXML
    private AnchorPane root;
    @FXML
    private Label nameText, descriptionText;
    @FXML
    private ImageView image;

    private User user;

    public RewardCell(User user) {
        super();
        this.user = user;
    }

    /**
     * updateItem: Updates the cell given the information of the reward object (include name, description and picture)
     *
     * @param reward : set cell given the reward object
     * @param empty
     */
    @Override
    protected void updateItem(Reward reward, boolean empty) {
        super.updateItem(reward, empty);
        setText(null);
        setGraphic(null);

        if (reward != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/namesayer/reward/RewardCell.fxml"));
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nameText.setText(reward.getRewardName());
            descriptionText.setText(reward.getRewardDescription());
            image.setImage(new Image(getClass().getResourceAsStream(reward.getImageURL())));
            setGraphic(root); //Setting image graphic

            //If current user points are less than reward requirements - make greyed out
            if (reward.getMinPoints() > user.getPoints()) {
                setOpacity(0.1);
            }

            //Update selected reward
            if (reward.isRedeemed()) {
                setStyle("-fx-border-color: #FF5252;" + "-fx-border-width: 3px;");
            }
        }
    }
}
