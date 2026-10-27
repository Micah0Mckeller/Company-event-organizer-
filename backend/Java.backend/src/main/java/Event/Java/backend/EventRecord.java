package Event.Java.backend;

import java.time.Instant;

public record EventRecord(String id, String name, String description, Instant createdAt) {
}
