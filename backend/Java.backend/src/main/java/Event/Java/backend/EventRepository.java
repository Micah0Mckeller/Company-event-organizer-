package Event.Java.backend;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {
    private final Queue<EventRecord> events = new ArrayDeque<>();

    public synchronized List<EventRecord> findAll() {
        return List.copyOf(events);
    }

    public synchronized EventRecord add(CreateEventRequest request) {
        EventRecord event = new EventRecord(
                UUID.randomUUID().toString(),
                request.name().trim(),
                request.description() == null ? "" : request.description().trim(),
                Instant.now());
        events.add(event);
        return event;
    }

    public synchronized boolean removeById(String id) {
        return events.removeIf(event -> event.id().equals(id));
    }
}
