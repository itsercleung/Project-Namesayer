package namesayer.login;

/**
 * Points: enumerated type returns the amount of points earned
 * in relation to what a user is earning it for.
 *
 * @author Kevin Xu
 */
public enum Points {
    PRACTISE_NAME(50),
    PRACTISE_CONCAT_NAME(100),
    CREATE_NAME(150),
    COMPARE_NAME(150);

    private final int points;

    Points(int points) {
        this.points = points;
    }

    public int getPoints() {
        return this.points;
    }
}
