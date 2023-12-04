package csc.buzybees;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Shift {
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final StringProperty details;

    public Shift(String date, String startTime, String endTime, String details) {
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.details = new SimpleStringProperty(details);
    }

    // Date property getter and setter
    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    // Start time property getter and setter
    public String getStartTime() {
        return startTime.get();
    }

    public void setStartTime(String startTime) {
        this.startTime.set(startTime);
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    // End time property getter and setter
    public String getEndTime() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime.set(endTime);
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    // Details property getter and setter
    public String getDetails() {
        return details.get();
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public StringProperty detailsProperty() {
        return details;
    }
}
