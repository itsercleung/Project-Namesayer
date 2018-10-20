package namesayer.login;

import java.util.HashSet;

/**
 * User describes the information related to a user.
 * A user has a username, points earned and rewards available.
 */
public class User {
    private String username;
    private int points;
    private HashSet<String> rewards;

    public User(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public User(String username, int points, HashSet<String> rewards) {
        this(username,points);
        this.rewards = rewards;
    }

    public String getUsername() {
        return this.username;
    }

    public int getPoints() {
        return this.points;
    }

    //Add points to existing points within individual user
    protected void addPoints(Points points) {this.points += points.getPoints();}

    public HashSet<String> getRewards() {
        return rewards;
    }

    /**
     * Add current rewards into HashSet
     * @param rewards : current rewards user has earned
     */
    public void setRewards(HashSet<String> rewards) {
        this.rewards = rewards;
    }

    @Override
    public String toString() {
       return this.username ;
    }

}
