package namesayer.util;

import org.controlsfx.control.Rating;

public class Name {
    private String name;
    private String created;
    private String date;
    private String time;
    private Rating rating;

    public Name(String name) {
        this.name = name;
    }

    public Name(String name, String created, String date, String time, Rating rating) {
        this.name = name;
        this.created = created;
        this.date = date;
        this.time = time;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public Rating getRating() {
        return rating;
    }

    public void setName(String name) { this.name = name; }

    public void setCreated(String created) { this.created = created; }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String toString() {
        return created + "_" + date + "_" + time + "_" + name + ".wav";
    }
}
