package namesayer.reward;

/**
 * Reward class describes the information of a reward including name, description, applied, redeemable, image
 * type and minimum points to earn such reward
 * @author Kevin Xu
 */
public class Reward {

    private String name;
    private String description;
    private boolean isRedeemed = false;
    private boolean isApplied = false;
    private boolean isRedeemable; //If award is unlockable (user manually does so) or automatically unlocked through set points
    private String imageURL;
    private int minPoints;
    private RewardManager.RewardType rewardType;

    //Achievements
    public Reward(String name, String description, int minPoints, String imageURL, RewardManager.RewardType rewardType) {
        this.name = name;
        this.description = description;
        this.minPoints = minPoints;
        this.imageURL = imageURL;
        isRedeemable = false;
        this.rewardType = rewardType;
    }

    //Unlockables
    public Reward(String name, String description, int minPoints, String imageURL, RewardManager.RewardType rewardType,
                  boolean isRedeemed, boolean isApplied) {
        this(name, description, minPoints, imageURL, rewardType);
        this.isRedeemed = isRedeemed;
        this.isApplied = isApplied;
        isRedeemable = true;
    }

    public String getRewardName() {
        return name;
    }

    public String getRewardDescription() {
        return description;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void redeemReward() {
        isRedeemed = true;
    }

    public void applyReward() {
        isApplied = true;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setIsRedeemable(boolean bool) {
        isRedeemable = bool;
    }

    public boolean getIsRedeemable() {
        return isRedeemable;
    }

    @Override
    public String toString() {
        return name;
    }
}

