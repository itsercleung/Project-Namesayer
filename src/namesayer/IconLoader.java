package namesayer;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import namesayer.login.User;

/**
 * IconLoader: Provides code UI icon loaders towards buttons and components towards most buttons throughout namesayer
 * controllers (includes sidebar, play bar, practise, record new etc).
 */
public class IconLoader {

    private User user;
    private Button rewardButton, helpButton, exitButton;
    private Button playButton, stopButton, prevButton, nextButton, recordButton;
    private Button recordSubButton, playOldButton, playNewButton, playCompare, saveButton;
    private Button listenButton, stopPlayButton;

    /**
     * Loads icons for the main menu items
     * @param user : current user logged in
     * @param rewardButton : rewardButton component
     * @param helpButton : helpButton component
     * @param exitButton : exitButton component
     */
    public IconLoader(User user, Button rewardButton, Button helpButton, Button exitButton) {
        this.user = user;
        this.rewardButton = rewardButton;
        this.helpButton = helpButton;
        this.exitButton = exitButton;
    }

    /**
     *
     */
    public IconLoader(User user, Button rewardButton, Button helpButton, Button exitButton,
                      Button playButton, Button stopButton, Button prevButton, Button nextButton, Button recordButton,
                      Button recordSubButton, Button playOld, Button playNew, Button playCompare, Button save) {
        this(user, rewardButton, helpButton, exitButton);
        this.playButton = playButton;
        this.stopButton = stopButton;
        this.prevButton = prevButton;
        this.nextButton = nextButton;
        this.recordButton = recordButton;
        this.recordSubButton = recordSubButton;
        this.playOldButton = playOld;
        this.playNewButton = playNew;
        this.playCompare = playCompare;
        this.saveButton = save;
    }

    /**
     * Loads icons for RecordNewController
     */
    public IconLoader(User user, Button rewardButton, Button helpButton, Button exitButton,
                      Button recordButton, Button saveButton, Button listenButton, Button stopPlayButton) {
        this(user, rewardButton, helpButton, exitButton);
        this.recordButton = recordButton;
        this.saveButton = saveButton;
        this.listenButton = listenButton;
        this.stopPlayButton = stopPlayButton;
    }

    /**
     * Loads all common icons for most menus
     * NOTE: currently based on icons on mainController
     */
    public void loadMenuIcons() {
        // Reward Button
        Image reward = new Image(getClass().getResourceAsStream("resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));

        // Help button
        Image help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));

        //Clippy setup if reward is set
        if (user.getRewards() == null) {
            help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));
        } else if (user.getRewards().contains("The Clippy Guide")) {
            help = new Image(getClass().getResourceAsStream("resources/icons/clippy.png"));
        } else {
            help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));
        }
        helpButton.setGraphic(new ImageView(help));

        // Logout Button
        Image logout = new Image(getClass().getResourceAsStream("resources/icons/sign-out.png"));
        exitButton.setGraphic(new ImageView(logout));
    }

    /**
     * Loads PlayController specific icons for buttons
     */
    public void loadPlayMenuIcons() {
        //Set icons to specific buttons from resources/icons (credited in description).
        //Set icons for play menu
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playButton.setGraphic(new ImageView(play));
        Image stop = new Image(getClass().getResourceAsStream("resources/icons/stop.png"));
        stopButton.setGraphic(new ImageView(stop));
        Image prev = new Image(getClass().getResourceAsStream("resources/icons/back.png"));
        prevButton.setGraphic(new ImageView(prev));
        Image next = new Image(getClass().getResourceAsStream("resources/icons/next.png"));
        nextButton.setGraphic(new ImageView(next));

        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordButton.setGraphic(new ImageView(rec));
        recordButton.setOnMouseEntered(e -> recordButton.setGraphic(new ImageView(recHover)));
        recordButton.setOnMouseExited(e -> recordButton.setGraphic(new ImageView(rec)));

        //Set icons for side menu
        Image recSub = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recSubHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordSubButton.setGraphic(new ImageView(recSub));
        Image playOld = new Image(getClass().getResourceAsStream("resources/icons/playOld.png"));
        Image playOldHover = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playOldButton.setGraphic(new ImageView(playOld));
        Image playNew = new Image(getClass().getResourceAsStream("resources/icons/playNew.png"));
        Image playNewHover = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        playNewButton.setGraphic(new ImageView(playNew));
        Image compare = new Image(getClass().getResourceAsStream("resources/icons/repeat.png"));
        Image compareHover = new Image(getClass().getResourceAsStream("resources/icons/repeatHover.png"));
        playCompare.setGraphic(new ImageView(compare));
        Image saveNew = new Image(getClass().getResourceAsStream("resources/icons/save.png"));
        Image saveNewHover = new Image(getClass().getResourceAsStream("resources/icons/saveHover.png"));
        saveButton.setGraphic(new ImageView(saveNew));

        //Setting styles for side menu bar buttons and EVENTS on hover
        recordSubButton.setFocusTraversable(false);
        playNewButton.setFocusTraversable(false);
        playOldButton.setFocusTraversable(false);
        saveButton.setFocusTraversable(false);
        playCompare.setFocusTraversable(false);

        //Setting mouse hover events
        recordSubButton.setOnMouseEntered(e -> {
            recordSubButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            recordSubButton.setGraphic(new ImageView(recSubHover));
        });
        recordSubButton.setOnMouseExited(e -> {
            recordSubButton.setStyle("-fx-background-color: transparent");
            recordSubButton.setGraphic(new ImageView(recSub));
        });

        playNewButton.setOnMouseEntered(e -> {
            playNewButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            playNewButton.setGraphic(new ImageView(playNewHover));
        });
        playNewButton.setOnMouseExited(e -> {
            playNewButton.setStyle("-fx-background-color: transparent");
            playNewButton.setGraphic(new ImageView(playNew));
        });

        playOldButton.setOnMouseEntered(e -> {
            playOldButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            playOldButton.setGraphic(new ImageView(playOldHover));
        });
        playOldButton.setOnMouseExited(e -> {
            playOldButton.setStyle("-fx-background-color: transparent");
            playOldButton.setGraphic(new ImageView(playOld));
        });

        playCompare.setOnMouseEntered(e -> {
            playCompare.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            playCompare.setGraphic(new ImageView(compareHover));
        });
        playCompare.setOnMouseExited(e -> {
            playCompare.setStyle("-fx-background-color: transparent");
            playCompare.setGraphic(new ImageView(compare));
        });

        saveButton.setOnMouseEntered(e -> {
            saveButton.setStyle("-fx-background-color: #FF5252;" + "-fx-text-fill: white;");
            saveButton.setGraphic(new ImageView(saveNewHover));
        });
        saveButton.setOnMouseExited(e -> {
            saveButton.setStyle("-fx-background-color: transparent");
            saveButton.setGraphic(new ImageView(saveNew));
        });
    }

    /**
     * Loads RecordNewController specific icons
     */
    public void loadRecordNewIcons() {
        //Set icons to specific buttons from resources/icons (credited in description).
        Image rec = new Image(getClass().getResourceAsStream("resources/icons/microphone.png"));
        Image recHover = new Image(getClass().getResourceAsStream("resources/icons/microphoneHover.png"));
        recordButton.setGraphic(new ImageView(rec));
        recordButton.setOnMouseEntered(e -> recordButton.setGraphic(new ImageView(recHover)));
        recordButton.setOnMouseExited(e -> recordButton.setGraphic(new ImageView(rec)));
        Image save = new Image(getClass().getResourceAsStream("resources/icons/saveHover.png"));
        saveButton.setGraphic(new ImageView(save));
        Image play = new Image(getClass().getResourceAsStream("resources/icons/play.png"));
        listenButton.setGraphic(new ImageView(play));
        Image stop = new Image(getClass().getResourceAsStream("resources/icons/stop.png"));
        stopPlayButton.setGraphic(new ImageView(stop));
    }
}
