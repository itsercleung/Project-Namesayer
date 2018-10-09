package namesayer.reward;

/**
 * Reward class describes the information of a reward
 */
public class Reward {
    public enum RewardType {
        // e.g.
        CLIPPY, BAD_WALLPAPER, BRONZE_TROPY
    }

    // i dunno what the implementation might look like
    public String getRewardName() {
        return null;
    }

    public RewardType getRewardType() {
        return RewardType.CLIPPY;
    }
}

