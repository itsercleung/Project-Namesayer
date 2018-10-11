package namesayer.reward;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.util.HashMap;

public class RewardBuilder {
    public static ObservableList<Reward> build() {
        ObservableList<Reward> rewards = FXCollections.observableArrayList();

        rewards.add(new Reward("Background Colour: Dark Blue", "Minimum points to unlock: 1000" +
                "\nThe background that should not be used"));
        rewards.add(new Reward("Dummy Reward II","Bad Description"));

        return rewards;
    }


}
