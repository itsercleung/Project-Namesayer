package namesayer.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.HashSet;

/**
 * UserCell: Has all the UI controller info about a listview cell for the User component in Login Controller. This stores
 * users profile picture, username and current points
 * @author Kevin Xu
 */
public class UserCell extends ListCell<User> {

    @FXML
    private AnchorPane root;
    @FXML
    private Text username = new Text("");
    @FXML
    private Text points = new Text("");
    @FXML
    private ImageView image;

    public UserCell() {
        super();
    }

    /**
     * Updates each cell in list with user info
     *
     * @param user  : Current user that is logged in to update for
     * @param empty : provide empty boolean input
     */
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        setText(null);
        setGraphic(null);

        //Load and set user cells
        if (user != null) {
            try {
                if (!user.getUsername().isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/namesayer/login/UserCell.fxml"));
                    loader.setController(this);
                    loader.load();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            username.setText(user.getUsername());
            points.setText(Integer.toString(user.getPoints()));

            //Sets the user profile image depending on their selected reward
            Image img;
            HashSet<String> rewards = user.getRewards();
            if (rewards == null) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/profile.png"));// no rewards
            } else if (rewards.contains("Bronze Trophy")) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/bronze.png"));
            } else if (rewards.contains("Silver Trophy")) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/silver.png"));
            } else if (rewards.contains("Gold Trophy")) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/gold.png"));
            } else if (rewards.contains("Platinum Trophy")) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/plat.png"));
            } else if (rewards.contains("The Clippy Guide")) {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/clippy.png"));
            } else {
                img = new Image(getClass().getResourceAsStream("/namesayer/resources/icons/profile.png"));
            }

            image.setImage(img);
            setGraphic(root);
        }
    }
}