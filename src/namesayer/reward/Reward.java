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
    private int minPoints = Integer.MAX_VALUE;

    public Reward(String name, String description, int minPoints) {
        this.name = name;
        this.description = description;
        this.minPoints = minPoints;
    }

    public Reward(String name, String description, int minPoints, boolean isRedeemed, boolean isApplied) {
        this(name,description,minPoints);
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

    public void setRedeemed(boolean bool) { isRedeemed = bool; }

    public void setApplied(boolean bool) { isApplied = bool; }

    public void redeemReward() {
        isRedeemed = true;
    }

    public void applyReward() {
        isApplied = true;
    }

    public int getMinPoints() { return minPoints; }

    @Override
    public String toString() {
        return name;
    }


    /*
    public RewardType getRewardType() {
        return RewardType.CLIPPY;
    }
    */
}

