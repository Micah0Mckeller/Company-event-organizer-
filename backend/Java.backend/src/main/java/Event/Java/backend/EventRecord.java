package Event.Java.backend;

import java.time.Instant;

/** Immutable event data returned by the event API. */
public record EventRecord(String id, String name, String description, Instant createdAt) {
}
