package namesayer.login;

import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

/**
 * Has all the UI info about a listview cell
 */
class UserCell extends ListCell<User> {
    private HBox hbox = new HBox();
    private Text username = new Text("");
    private Text points = new Text("");
    private Pane pane = new Pane();
    // TODO possibly add trophy reward image for user (if they earned it) here

    public UserCell() {
        super();
        hbox.getChildren().addAll(username, pane,points);
        hbox.setHgrow(pane, Priority.ALWAYS);

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
            username.setText(user.getUsername());
            points.setText(Integer.toString(user.getPoints()));
            setGraphic(hbox);
        }
    }
}