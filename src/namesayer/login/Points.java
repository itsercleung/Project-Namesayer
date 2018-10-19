package namesayer.login;

public enum Points {
    PRACTISE_NAME (100),
    PRACTISE_CONCAT_NAME (150),
    CREATE_NAME (250),
    COMPARE_NAME(200);

    private final int points;

    Points(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }
}
