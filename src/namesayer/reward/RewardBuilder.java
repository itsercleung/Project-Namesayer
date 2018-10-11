package namesayer.reward;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.util.HashMap;

/**
 * Creates the list of rewards to populate a listview
 */
public class RewardBuilder {

    public static ObservableList<Reward> build() {
        ObservableList<Reward> rewards = FXCollections.observableArrayList();

        rewards.add(new Reward("Bronze Trophy","You have earned enough points \nto earn you a Bronze Trophy!",20000));
        rewards.add(new Reward("Silver Trophy","You have earned enough points \nto earn you a Silver Trophy!",50000));
        rewards.add(new Reward("Gold Trophy","You have earned enough points \nto earn you a Gold Trophy!",100000));
        rewards.add(new Reward("Diamond Trophy","You have earned enough points \nto earn you a Diamond Trophy!",200000));
        rewards.add(new Reward("Background Colour: Dark Blue", "Minimum points to unlock: 1000" +
                "\nThe background that should not be used",1000));
        rewards.add(new Reward("Dummy Reward","Bad Description",0));
        rewards.add(new Reward("Random Reward", "Thank You", 10000));

        return rewards;
    }
}
