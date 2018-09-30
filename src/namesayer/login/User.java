package namesayer.login;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class User {
    private String username;
    private int points;

    public User(String username, int score) {
        this.username = username;
        this.points = score;
    }

    public String getUsername() {
        return this.username;
    }

    public int getPoints() {
        return this.points;
    }

    public void updateScore(Points points) {
        this.points += points.getPoints();
    }

    // further down the track reward system?
    public void unlocks() {

    }

    // save user to file (should be result of button click...)
    public void saveUser() throws IOException {
        File directory = new File("./data/usernames");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String path = "./data/usernames/"+username;
        File usernameTxt = new File(path);
        if(usernameTxt.exists()) {
            return;
        }

        // add username and points
        FileWriter fw = new FileWriter(usernameTxt);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(username);
        bw.newLine();
        bw.write(0);
        bw.close();
        fw.close();
    }

    @Override
    public String toString() {
       return this.username ;
    }

    public void updateUser() {

    }

}
