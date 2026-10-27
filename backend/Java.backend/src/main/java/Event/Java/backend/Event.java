
package Event.Java.backend;

import java.time.LocalDate;
import java.util.UUID;

public class Event {
    private final String eventDescription;
    private final String eventDate;
    private final String eventLocation;
    private final String eventName;
    private final LocalDate eventTimeSubmitted;

    public Event() {
    }

    public Event(String eventName, String eventDescription, String eventDate,
            String eventLocation, LocalDate eventTimeSubmitted) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventTimeSubmitted = eventTimeSubmitted;
    }

    public void setDescription(String description) {
        this.eventDescription = description;
    }

    public String getDescription() {
        return eventDescription;
    }

    public void setDate(String date) {
        this.eventDate = date;
    }

    public String getDate() {
        return eventDate;
    }

    public void setLocation(String location) {
        this.eventLocation = location;
    }

    public String getLocation() {
        return eventLocation;
    }

    public void setName(String name) {
        this.eventName = name;
    }

    public String getName() {
        return eventName;
    }

    public LocalDate getTimeSubmitted() {
        return eventTimeSubmitted;
    }

    public void setTimeSubmitted(LocalDate timeSubmitted) {
        this.eventTimeSubmitted = timeSubmitted;
    }
}
