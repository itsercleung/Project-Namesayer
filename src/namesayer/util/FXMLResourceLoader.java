package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

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
