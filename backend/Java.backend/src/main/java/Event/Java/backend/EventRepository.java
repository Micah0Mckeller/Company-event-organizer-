package Event.Java.backend;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {
    private final CustomHashMap<String, EventRecord> events = new CustomHashMap<>();

    /** Returns an immutable snapshot of the events in submission order. */
    public synchronized List<EventRecord> findAll() {
        return List.copyOf(events.values());
    }

    /** Finds an event by its UUID. */
    public synchronized Optional<EventRecord> findById(String id) {
        return Optional.ofNullable(events.get(id));
    }

    /** Creates and appends an event from the submitted request. */
    public synchronized EventRecord add(CreateEventRequest request) {
        EventRecord event = new EventRecord(
                UUID.randomUUID().toString(),
                request.name().trim(),
                request.description() == null ? "" : request.description().trim(),
                Instant.now());
        events.put(event.id(), event);
        return event;
    }

    /** Removes the event with the given ID, returning whether one was found. */
    public synchronized boolean removeById(String id) {
        return events.remove(id) != null;
    }
}
