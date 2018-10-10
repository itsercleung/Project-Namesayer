package namesayer.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Has all the UI info about a listview cell
 */
public class UserCell extends ListCell<User> {

    private FXMLLoader loader;

    @FXML private AnchorPane root;
    @FXML private Text username = new Text("");
    @FXML private Text points = new Text("");
    // TODO possibly add trophy reward image for user (if they earned it) here
    @FXML private ImageView image;

    public UserCell() {
        super();
    }

    /**
     * Updates each cell in list with user info
     * @param user
     * @param empty
     */
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        setText(null);
        setGraphic(null);

        if (user != null) {
            try {
                loader = new FXMLLoader(getClass().getResource("../login/UserCell.fxml"));
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            username.setText(user.getUsername());
            points.setText(Integer.toString(user.getPoints()));
            Image img = new Image(getClass().getResourceAsStream("../resources/icons/profile.png"));
            image.setImage(img);
            setGraphic(root);
        }


    }
}