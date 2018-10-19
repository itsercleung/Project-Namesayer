package namesayer;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconLoader {

    private Button rewardButton, helpButton,exitButton;

    public IconLoader(Button rewardButton, Button helpButton, Button exitButton) {
        this.rewardButton = rewardButton;
        this.helpButton = helpButton;
        this.exitButton = exitButton;
    }


    /**
     * Loads all common icons for most menus
     */
    // NOTE: currently based on icons on maincontroller
    public void loadMenuIcons() {
        // Reward Button
        Image reward = new Image(getClass().getResourceAsStream("resources/icons/rewards.png"));
        Image rewardHover = new Image(getClass().getResourceAsStream("resources/icons/rewardsHover.png"));
        rewardButton.setGraphic(new ImageView(reward));
        rewardButton.setOnMouseEntered(e -> rewardButton.setGraphic(new ImageView(rewardHover)));
        rewardButton.setOnMouseExited(e -> rewardButton.setGraphic(new ImageView(reward)));

        // Help button
        Image help = new Image(getClass().getResourceAsStream("resources/icons/info.png"));
        helpButton.setGraphic(new ImageView(help));

        // Logout Button
        Image logout = new Image(getClass().getResourceAsStream("resources/icons/sign-out.png"));
        exitButton.setGraphic(new ImageView(logout));
    }

    /**
     * Loads all icons for buttons, specifically in the PlayController
     */
    public void loadPlayMenuIcons() {

    }
}
