package namesayer.login;

import namesayer.reward.Reward;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

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

    protected void addPoints(Points points) {this.points += points.getPoints();}

    public HashSet<String> getRewards() {
        return rewards;
    }

    public void setRewards(HashSet<String> rewards) {
        this.rewards = rewards;
    }

    public void addReward(String reward) { rewards.add(reward); }

    public void removeReward(String reward) { rewards.remove(reward); }

    @Override
    public String toString() {
       return this.username ;
    }

}
