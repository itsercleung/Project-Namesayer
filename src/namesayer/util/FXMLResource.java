package namesayer.util;

public enum FXMLResource {
    LOGIN("../resources/Login.fxml"),
    MAIN("../resources/Main.fxml"),
    PLAY("../resources/Play.fxml"),
    PRACTISE("../resources/Practise.fxml"),
    RECORD_NEW("../resources/RecordNew.fxml"),
    REWARD("../resources/Reward.fxml"),
    TEST_MICROPHONE("../resources/TestMicrophone.fxml"),
    LOGOUT("../resources/Login.fxml");

    private String resource;
    FXMLResource(String resource) { this.resource = resource; }
    public String getResourceURL() {return resource;}
}