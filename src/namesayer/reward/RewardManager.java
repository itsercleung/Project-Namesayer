package namesayer.reward;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.login.User;
import namesayer.login.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * RewardManager: Creates the list of rewards to populate a listView. Can be edited easily through generateRewards() method
 * which can customize reward descriptions, titles and images as well as adding additional rewards.
 *
 * @author Kevin Xu
 */
public class RewardManager {

    public enum RewardType {TROPHY, CLIPPY}

    private ObservableList<Reward> rewards;
    private List<Reward> rewardList = new ArrayList<>();
    private User user;

    public RewardManager(User user) {
        generateRewards();
        this.user = user;
    }

    /**
     * Static rewards generated through generateRewards method - can easily add or remove within such method
     */
    private void generateRewards() {
        rewards = FXCollections.observableArrayList();
        rewards.add(new Reward("Bronze Trophy",
                "You have earned 1,000 points \nto earn you a Bronze Trophy!",
                1000, "/namesayer/resources/icons/bronze.png",
                RewardType.TROPHY));
        rewards.add(new Reward("Silver Trophy",
                "You have earned 5,000 points \nto earn you a Silver Trophy!",
                5000, "/namesayer/resources/icons/silver.png",
                RewardType.TROPHY));
        rewards.add(new Reward("Gold Trophy",
                "You have earned 10,000 points \nto earn you a Gold Trophy!",
                10000,
                "/namesayer/resources/icons/gold.png",
                RewardType.TROPHY));
        rewards.add(new Reward("The Clippy Guide",
                "   Unlock Clippy for 20,000 points!\n   Will guide your way to Platinum!",
                20000, "/namesayer/resources/icons/clippy.png",
                RewardType.CLIPPY));
        rewards.add(new Reward("Platinum Trophy",
                "You have earned 100,000 points \nto earn you a Platinum Trophy!",
                100000,
                "/namesayer/resources/icons/plat.png",
                RewardType.TROPHY));
        rewardList.addAll(rewards);
    }

    public List<Reward> getRewardList() {
        return rewardList;
    }

    /**
     * Creates a list of Rewards based on what the user has redeemed or applied
     *
     * @return returns the reward list for RewardController to display
     */
    public ObservableList<Reward> build() {
        for (Reward reward : rewards) {
            String name = reward.getRewardName(); // if already redeemed

            if (reward.getMinPoints() <= user.getPoints()) {
                reward.setIsRedeemable(true);
            }

            // if reward is already redeemed
            if (user.getRewards() != null) {
                if (user.getRewards().contains(name + "*")) {
                    reward.applyReward(); // "reward*" is applied reward
                    reward.setIsRedeemable(false);
                } else if (user.getRewards().contains(name)) {
                    reward.redeemReward(); // "reward" is just redeemed
                    reward.setIsRedeemable(false);
                }
            }
        }
        return rewards;
    }

    /**
     * applyReward: Updates users current applied reward to profile picture and other additional changes
     *
     * @param reward : given the reward selected
     */
    public void applyReward(Reward reward) {
        reward.applyReward();
        UserUtils.updateUserRewards(user, reward);
        reward.setIsRedeemable(false);
    }

    /**
     * redeemReward: Updates users redeem rewarded to be unlocked
     *
     * @param reward : given the reward selected
     */
    public void redeemReward(Reward reward) {
        reward.redeemReward();
        UserUtils.updateUserRewards(user, reward);
        reward.setIsRedeemable(false);
    }
}
