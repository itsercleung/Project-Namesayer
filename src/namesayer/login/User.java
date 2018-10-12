package namesayer.login;

import namesayer.reward.Reward;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class User {
    private String username;
    private int points;
    private List<String> rewards;

    public User(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public User(String username, int points, List<String> rewards) {
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

    public List<String> getRewards() {
        return rewards;
    }

    @Override
    public String toString() {
       return this.username ;
    }

}
