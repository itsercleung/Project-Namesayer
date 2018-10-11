package namesayer.reward;

/**
 * Reward class describes the information of a reward
 *
 */
public class Reward {
    /*
    public enum RewardType {
        // e.g.
        CLIPPY, BAD_WALLPAPER, BRONZE_TROPY
    }
    */

    private String name;
    private String description;
    private boolean isRedeemed = false;
    private boolean isApplied = false;


    public Reward(String name, String description, boolean isRedeemed, boolean isApplied) {
        this.name = name;
        this.description = description;
        this.isRedeemed = isRedeemed;
        this.isApplied = isApplied;
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

    public boolean isApplied() {
        return isApplied;
    }

    public void redeemReward() {
        isRedeemed = true;
    }

    public void applyReward() {
        isApplied = true;
    }

    /*
    public RewardType getRewardType() {
        return RewardType.CLIPPY;
    }
    */
}

