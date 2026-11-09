
package Event.Java.backend;

import java.time.LocalDate;
import java.util.UUID;

/** Holds the name, description, date, location, and submission date of an event. */
public class Event {
    private final String eventDescription;
    private final String eventDate;
    private final String eventLocation;
    private final String eventName;
    private final LocalDate eventTimeSubmitted;

    /** Creates an event with fields left unset. */
    public Event() {
    }

    /** Creates an event initialized with all of its event details. */
    public Event(String eventName, String eventDescription, String eventDate,
            String eventLocation, LocalDate eventTimeSubmitted) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventTimeSubmitted = eventTimeSubmitted;
    }

    /** Sets the event description. */
    public void setDescription(String description) {
        this.eventDescription = description;
    }

    /** Returns the event description. */
    public String getDescription() {
        return eventDescription;
    }

    /** Sets the event date. */
    public void setDate(String date) {
        this.eventDate = date;
    }

    /** Returns the event date. */
    public String getDate() {
        return eventDate;
    }

    /** Sets the event location. */
    public void setLocation(String location) {
        this.eventLocation = location;
    }

    /** Returns the event location. */
    public String getLocation() {
        return eventLocation;
    }

    /** Sets the event name. */
    public void setName(String name) {
        this.eventName = name;
    }

    /** Returns the event name. */
    public String getName() {
        return eventName;
    }

    /** Returns the date when the event was submitted. */
    public LocalDate getTimeSubmitted() {
        return eventTimeSubmitted;
    }

    /** Sets the date when the event was submitted. */
    public void setTimeSubmitted(LocalDate timeSubmitted) {
        this.eventTimeSubmitted = timeSubmitted;
    }
}
