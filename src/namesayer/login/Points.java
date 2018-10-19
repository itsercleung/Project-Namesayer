package namesayer.login;

/**
 * Points enumerated type returns the amount of points earned
 * in relation to what a user is earning it for.
 */
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
