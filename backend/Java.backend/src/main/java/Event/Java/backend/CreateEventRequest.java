package Event.Java.backend;

/** Carries the name and optional description supplied when creating an event. */
public record CreateEventRequest(String name, String description) {
}
