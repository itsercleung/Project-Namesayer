package namesayer.reward;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import namesayer.login.User;
import namesayer.login.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates the list of rewards to populate a listview
 */
public class RewardManager {

    private ObservableList<Reward> rewards;
    private List<Reward> rewardList = new ArrayList<>();
    private User user;

    public RewardManager(User user) {
        generateRewards();
        this.user = user;
    }

    //Static rewards generated through generateRewards method - can easily add or remove within such method
    private void generateRewards() {
        rewards = FXCollections.observableArrayList();
        rewards.add(new Reward("Bronze Trophy",
                "You have earned 1,000 points \nto earn you a Bronze Trophy!",
                1000, "../resources/icons/bronze.png"));
        rewards.add(new Reward("Silver Trophy",
                "You have earned 5,000 points \nto earn you a Silver Trophy!",
                5000,"../resources/icons/silver.png"));
        rewards.add(new Reward("Gold Trophy",
                "You have earned 10,000 points \nto earn you a Gold Trophy!",
                10000,"../resources/icons/gold.png"));
        rewards.add(new Reward("Platinum Trophy",
                "You have earned 100,000 points \nto earn you a Platinum Trophy!",
                100000, "../resources/icons/plat.png"));
        rewards.add(new Reward( "    The Clippy Guide",
                "    Unlock Clippy for 20,000 points!\n    Will guide your way to Platinum!",
                20000, "../resources/icons/clippy.png", false, false));
        rewardList.addAll(rewards);
    }

    public List<Reward> getRewardList() {
        return rewardList;
    }

    /**
     * Creates a list of Rewards based on what the user has redeemed or applied
     * @return
     */
    public ObservableList<Reward> build() {
        for (Reward reward :rewards) {
            String name = reward.getRewardName(); // if redeemed

            if (user.getRewards() != null) {
                if (user.getRewards().contains(name + "*")) {
                    reward.applyReward(); // "reward*" is applied reward
                } else if (user.getRewards().contains(name)) {
                    reward.redeemReward(); // "reward" is just redeemed
                }
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
