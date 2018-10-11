package namesayer.login;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.*;
import java.util.Scanner;

/**
 * Call these utility functions via UserUtils.<method>()
 */
public class UserUtils {

    /**
     * Updates user list in main menu
     * @param userList
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
                    String username = reader.next();
                    int points = Integer.valueOf(reader.next());
                    users.add(new User(username, points));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            userList.setItems(users);
        }
    }

    /**
     * Sets current user upon logging in
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

        }
    }

    /**
     * getCurrentLoginUser gets and updates the currently selected user,
     * for each new view
     */
    public static User getCurrentLoginUser(Text userText, Text pointsText) {
        // TODO should use string split to get user information.. to make things easier
        String path = "./data/CurrentUser.txt";
        File currentUser = new File(path);

        try {
            // get current user name
            Scanner reader = new Scanner(currentUser);
            String username = reader.next();
            reader.close();

            // get user info from data
            path = "./data/usernames/"+username+".txt";
            File userInfo = new File(path);
            reader = new Scanner(userInfo);
            reader.nextLine();
            String number = reader.next();

            int points = Integer.parseInt(number);
            reader.close();

            userText.setText(username);
            pointsText.setText("Points: "+points);
            return new User(username, points);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

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
            bw.append(name);
            bw.newLine();
            bw.append("0");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * updateUser sets or updates user information on the Text
     * at bottom left corner.
     */
    public static void updateUser(User user, Text userText, Text pointsText) {
        userText.setText("User: " + user.getUsername());
        pointsText.setText("Points: " + user.getPoints());

        String path = "./data/usernames/" + user.getUsername() + ".txt";
        File usernameTxt = new File(path);

        try {
            FileWriter fw = new FileWriter(usernameTxt, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(user.getUsername());
            bw.newLine();
            bw.write(String.valueOf(user.getPoints()));
            bw.close();
        } catch (IOException ioe) {

        }
    }

    /**
     * updateUser will add points to user and updates the GUI points
     */
    public static void updateUser(User user, Points points, Text userText, Text pointsText) {
        user.addPoints(points);
        UserUtils.updateUser(user,userText,pointsText);
    }
}
