package namesayer.util;

import org.controlsfx.control.Rating;

//NAME Class: Stores all properties of each audio file. Can use tableView to provide information, or if we need to compare
//such objects, or if we need to rename an audio name file appropriately using toString().
public class Name {
    private String name;
    private String created;
    private String date;
    private String time;
    private Rating rating;
    private String createdDesc;

    public Name(String name) {
        this.name = name;
    }

    public Name(String name, String created, String date, String time, Rating rating) {
        this.name = name;
        this.created = created;
        this.date = date;
        this.time = time;
        this.rating = rating;

        //Setting created to single name (depending if name is single or concat)
        if (!created.contains(" ")) {
            createdDesc = created;
        }
        else if (created.contains(" ")) {
            createdDesc = "[COMBINED]";
        }
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public String getDate() { return date; }

    public String getTime() { return time; }

    public Rating getRating() {
        return rating;
    }

    public String getCreatedDesc() {
        return createdDesc;
    }

    public void setName(String name) { this.name = name; }

    public void setCreated(String created) { this.created = created; }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    //If name is concat, we can replace the createdBy section with [COMBINATION] of users choice
    public String replaceDesc() { return date + "_" + time + "_" + name; }

    public String toString() {
        return created + "_" + date + "_" + time + "_" + name;
    }
}
