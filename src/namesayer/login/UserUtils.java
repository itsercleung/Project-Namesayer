package namesayer.login;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import namesayer.reward.Reward;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * UserUtils: is a group of functions that will deal with points,
 * user and reward information.
 * Call these utility functions via UserUtils.<method>()
 *
 * @author Kevin Xu
 */
public class UserUtils {

    /**
     * Updates user list in main menu
     *
     * @param userList : list of all users in current system database
     */
    public static void updateUserList(JFXListView<User> userList) {
        File directory = new File("./data/usernames");
        if (!directory.exists()) {
            directory.mkdir();
        }

        File[] dirfiles = directory.listFiles();
        ObservableList<User> users = FXCollections.observableArrayList();
        Scanner reader;

        if (dirfiles != null) {
            for (File file : dirfiles) {
                try {
                    // gets user name and points, adds to listview
                    reader = new Scanner(file);
                    String[] user = reader.nextLine().split("~");

                    String username = user[0];
                    int points = Integer.valueOf(user[1]);

                    HashSet<String> rewards = null;

                    // get rewards
                    if (user.length > 2) {
                        String[] subset = Arrays.copyOfRange(user, 2, user.length);
                        rewards = new HashSet<>(Arrays.asList(subset));
                    }

                    users.add(new User(username, points, rewards));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            userList.setItems(users);
        }
    }

    /**
     * Sets current user upon logging in
     *
     * @param user : Current user which has been selected by user of namesayer
     */
    public static void setCurrentLoginUser(User user) {
        String path = "./data/CurrentUser.txt";
        File currentUser = new File(path);
        try {
            FileWriter fw = new FileWriter(currentUser, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(user.getUsername());
            bw.close();
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * getCurrentLoginUser gets and updates the currently selected user for each new view
     *
     * @param userText   : Text component that displays username in text
     * @param pointsText : Text component that displays users points in text
     */
    public static User getCurrentLoginUser(Text userText, Text pointsText) {
        String path = "./data/CurrentUser.txt";
        File currentUser = new File(path);

        try {
            // get current user name
            Scanner reader = new Scanner(currentUser);
            String username = reader.next();
            reader.close();

            // get user info from data
            path = "./data/usernames/" + username + ".txt";
            File userInfo = new File(path);
            reader = new Scanner(userInfo);
            String[] user = reader.nextLine().split("~");

            int points = Integer.parseInt(user[1]);

            HashSet<String> rewards = null;

            // get rewards
            if (user.length > 2) {
                String[] subset = Arrays.copyOfRange(user, 2, user.length);
                rewards = new HashSet<>(Arrays.asList(subset));
            }
            reader.close();
            userText.setText(username);
            pointsText.setText("Points: " + points);
            return new User(username, points, rewards);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Creates user and associated txt file.
     *
     * @param name : name of created new username
     * @return
     */
    public static boolean createUser(String name) {
        String path = "./data/usernames/" + name + ".txt";
        File usernameTxt = new File(path);
        if (usernameTxt.exists()) {
            // give warning and not exit
            return false;
        }

        // add username and points
        try {
            FileWriter fw = new FileWriter(usernameTxt, true);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = name + "~0";
            bw.append(line);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * updateUser sets or updates user information on the Text
     * at top left corner.
     *
     * @param user       : current selected user
     * @param userText   : text component displaying username
     * @param pointsText : text component displaying points
     */
    public static void updateUser(User user, Text userText, Text pointsText) {
        userText.setText(user.getUsername());
        pointsText.setText("Points: " + user.getPoints());

        String path = "./data/usernames/" + user.getUsername() + ".txt";
        File usernameTxt = new File(path);

        //Read username text file and get users points and name and apply to text components
        try {
            FileWriter fw = new FileWriter(usernameTxt, false);
            BufferedWriter bw = new BufferedWriter(fw);
            String updateString = user.getUsername() + "~" + String.valueOf(user.getPoints());

            if (user.getRewards() == null) {
                bw.write(updateString);
                bw.close();
                return;
            }

            for (String r : user.getRewards()) {
                updateString = updateString + "~" + r;
            }

            bw.write(updateString);
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * updateUser will add points to user and updates the GUI points
     */
    public static void updateUser(User user, Points points, Text userText, Text pointsText) {
        user.addPoints(points);
        UserUtils.updateUser(user, userText, pointsText);
    }


    /**
     * updateUserRewards will disable or enable the current input reward.
     *
     * @param user   : current selected user
     * @param reward : current selected reward to apply
     */
    public static void updateUserRewards(User user, Reward reward) {
        String path = "./data/usernames/" + user.getUsername() + ".txt";
        File usernameTxt = new File(path);

        try {
            FileWriter fw = new FileWriter(usernameTxt, false);
            BufferedWriter bw = new BufferedWriter(fw);
            String updateString = user.getUsername() + "~" + String.valueOf(user.getPoints());

            String rewardName = reward.getRewardName();

            // if user has no rewards active
            if (user.getRewards() == null) {
                updateString += "~" + rewardName;
                HashSet<String> rewards = new HashSet<>();
                rewards.add(rewardName);
                user.setRewards(rewards);

                bw.write(updateString);
                bw.close();
                fw.close();
                return;
            }

            HashSet<String> addOrRemoveSet = new HashSet<>();

            addOrRemoveSet.add(rewardName);
            updateString = updateString + "~" + rewardName;
            user.setRewards(addOrRemoveSet);
            bw.write(updateString);
            bw.close();
            fw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
