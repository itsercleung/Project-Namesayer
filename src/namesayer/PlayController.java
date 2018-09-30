package namesayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayController implements Initializable {

    @FXML private AnchorPane mainRoot;
    @FXML private TableView<?> nameTable;
    @FXML private TableColumn<?, ?> nameCol;
    @FXML private TableColumn<?, ?> createdCol;
    @FXML private TableColumn<?, ?> dateCol;
    @FXML private TableColumn<?, ?> timeCol;
    @FXML private TableColumn<?, ?> ratingCol;
    @FXML private Label playLabel;
    @FXML private Button exitButton;
    @FXML private Button prevButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private Button nextButton;
    @FXML private Button recordButton;
    @FXML private Rating audioRating;

    @FXML
    void exitPressed(ActionEvent event) {
        AnchorPane practiseRoot = null;
        try {
            practiseRoot = FXMLLoader.load(getClass().getResource("resources/Practise.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainRoot.getChildren().setAll(practiseRoot);
    }

    @FXML
    void nextPressed(ActionEvent event) {

    }

    @FXML
    void playPressed(ActionEvent event) {

    }

    @FXML
    void prevPressed(ActionEvent event) {

    }

    @FXML
    void recordPressed(ActionEvent event) {

    }

    @FXML
    void stopPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set icons to specific buttons from resources/icons (credited in description).
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playButton.setGraphic(new ImageView(play));
        Image stop = new Image(getClass().getResourceAsStream("resources/icons/stop.png"));
        stopButton.setGraphic(new ImageView(stop));
        Image prev = new Image(getClass().getResourceAsStream("resources/icons/back.png"));
        prevButton.setGraphic(new ImageView(prev));
        Image next = new Image(getClass().getResourceAsStream("resources/icons/next.png"));
        nextButton.setGraphic(new ImageView(next));
        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        recordButton.setGraphic(new ImageView(rec));
    }
}