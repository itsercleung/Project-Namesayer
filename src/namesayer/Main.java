package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

/**
 * Main initialises the entire NameSayer program.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        // create folders
        File dataFolder = new File("./data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
            new File("./data/names").mkdir();
            new File ("./data/usernames").mkdir();
            new File("./data/currentUser.txt").createNewFile();
        }

        Parent root = FXMLLoader.load(getClass().getResource("resources/Login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("NameSayer");
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setResizable(false);
        primaryStage.show();
        root.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
