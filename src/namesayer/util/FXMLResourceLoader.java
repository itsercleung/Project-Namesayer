package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * FXMLResourceLoader: allows the loading of each of the menu views.
 * It takes in FXMLResource which has the path of the location of the FXML file.
 */
public class FXMLResourceLoader {
    public void load(FXMLResource resource, Pane beforeRoot, Pane afterRoot) {
        try {
            beforeRoot = FXMLLoader.load(getClass().getResource(resource.getResourceURL()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        afterRoot.getChildren().setAll(beforeRoot);
    }

}
