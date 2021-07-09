package sg.edu.rp.c346.id19023702.demodatabase;

import java.io.Serializable;

public class Task implements Serializable {
    private int _id;
    private String description;
    private String date;

    // DB INSERTION
    public Task(String description, String date) {
        this.description = description;
        this.date = date;
    }

    public Task(int _id,String description, String date) {
        this._id = _id;
        this.description = description;
        this.date = date;
    }

    public int get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Song{" +
                ", id=" + _id +
                ", description=" + description +
                ", date=" + date +
                '}';
    }
}
