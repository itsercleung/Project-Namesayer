package namesayer.login;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    private String username;
    private int points;

    public User(String username, int points) {
        this.username = username;
        this.points = points;
    }

    public String getUsername() {
        return this.username;
    }

    public int getPoints() {
        return this.points;
    }

    public void addPoints(Points points) {this.points += points.getPoints();}
/* Deprecated: moved to userutils
    public void updateScore(Points points) {
        this.points += points.getPoints();
    }
*/
    // further down the track reward system?
    public void unlocks() {

    }

    @Override
    public String toString() {
       return this.username ;
    }

}
