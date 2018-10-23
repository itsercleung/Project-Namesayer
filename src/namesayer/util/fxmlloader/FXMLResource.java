package namesayer.util.fxmlloader;

/**
 * FXMLResource: Allows for calling constant fxml menu scenes for each scene change
 *
 * @author Kevin Xu
 */
public enum FXMLResource {
    LOGIN("/namesayer/resources/Login.fxml"),
    MAIN("/namesayer/resources/Main.fxml"),
    PLAY("/namesayer/resources/Play.fxml"),
    PRACTISE("/namesayer/resources/Practise.fxml"),
    RECORD_NEW("/namesayer/resources/RecordNew.fxml"),
    REWARD("/namesayer/resources/Reward.fxml"),
    TEST_MICROPHONE("/namesayer/resources/TestMicrophone.fxml"),
    LOGOUT("/namesayer/resources/Login.fxml");

    private String resource;

    FXMLResource(String resource) {
        this.resource = resource;
    }

    public String getResourceURL() {
        return resource;
    }
}