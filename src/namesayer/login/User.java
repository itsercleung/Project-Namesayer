package namesayer.login;

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

    // save user to file
    public void saveUser() {

    }

    public void updateUser() {

    }

}
