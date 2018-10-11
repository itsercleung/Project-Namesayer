package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FXMLResourceLoader {
    public void load(FXMLResource resource, Pane fromRoot, Pane toRoot) {
        try {
            fromRoot = FXMLLoader.load(getClass().getResource(resource.getResource()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        toRoot.getChildren().setAll(fromRoot);
    }

}
