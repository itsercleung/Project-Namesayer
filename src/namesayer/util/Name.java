package namesayer.util;

public class Name {
    private String name;
    private String created;
    private String date;
    private String time;

    public Name(String name, String created, String date, String time) {
        this.name = name;
        this.created = created;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getCreated() {
        return created;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setName(String name) { this.name = name; }

    public void setCreated(String created) { this.created = created; }

    public void setDate(String date) { this.date = date; }

    public void setTime(String time) { this.time = time; }

}
