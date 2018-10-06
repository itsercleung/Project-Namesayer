package namesayer.login;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.io.*;
import java.util.Scanner;

public class UserUtils {

    // call these utility functions via UserUtils.<method>()

    public static void getUserList(ListView<User> userList) {
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
        //return userList;
    }

    public static void setCurrentLoginUser(User user) {
        String path = "./data/CurrentUser.txt";
        File currentUser = new File(path);
        try {
            FileWriter fw = new FileWriter(currentUser, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(user.getUsername());
            bw.close();
        } catch (IOException ioe) {

        }
    }

    public static User getCurrentLoginUser() {
        String path = "./data/CurrentUser.txt";
        File currentUser = new File(path);
        File directory = new File("./data/usernames");
        File[] dirfiles = directory.listFiles();
        Scanner reader;

        try {
            reader = new Scanner(currentUser);
            String username = reader.next();
            reader.close();

            for (File file : dirfiles) {
                if (file.getName().equals(username)) {
                    int points = Integer.valueOf(reader.next());
                    return new User(username, points);
                }
            }

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
        } catch (IOException ioe) {

        }

        return true;
    }

    // sets or updates user information on the Text at bottom left corner
    public static void updateUser(Text username, Text points, User user) {
        username.setText("User: " + user.getUsername());
        points.setText("Points: " + user.getPoints());
    }
}
