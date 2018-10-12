package namesayer.reward;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import namesayer.login.User;
import namesayer.login.UserUtils;

/**
 * Creates the list of rewards to populate a listview
 */
public class RewardManager {

    private ObservableList<Reward> rewards;
    private User user;

    public RewardManager(User user) {
        generateRewards();
        this.user = user;
    }

    private void generateRewards() {
        rewards = FXCollections.observableArrayList();
        rewards.add(new Reward("Bronze Trophy",
                "You have earned enough points \nto earn you a Bronze Trophy!",
                20000));
        rewards.add(new Reward("Silver Trophy",
                "You have earned enough points \nto earn you a Silver Trophy!",
                50000));
        rewards.add(new Reward("Gold Trophy",
                "You have earned enough points \nto earn you a Gold Trophy!",
                100000));
        rewards.add(new Reward("Diamond Trophy",
                "You have earned enough points \nto earn you a Diamond Trophy!",
                200000));
    }

    /**
     * Creates a list of Rewards based on what the user has redeemed or applied
     * @return
     */
    public ObservableList<Reward> build() {
        for (Reward reward :rewards) {
            String name = reward.getRewardName(); // if redeemed

            if (user.getRewards().contains(name + "*")) {
                reward.applyReward(); // "reward*" is applied reward
            }
            else if (user.getRewards().contains(name)) {
                reward.redeemReward(); // "reward" is just redeemed
            }
        }

        return rewards;
    }

    public void applyReward(Reward reward) {
        reward.applyReward();
        // TODO unapply previous reward

        // TODO apply reward

        // TODO update in user text file
        UserUtils.updateUserRewards(user, reward);

    }

    public void redeemReward(Reward reward) {
        reward.redeemReward();

        // TODO update in user text file
        UserUtils.updateUserRewards(user,reward);
    }
}
