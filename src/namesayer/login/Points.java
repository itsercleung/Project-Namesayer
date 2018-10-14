package namesayer.login;

public enum Points {
    PRACTISE_NAME (100),
    PRACTISE_CONCAT_NAME (200),
    CREATE_NAME (300);

    private final int points;

    Points(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }
}
