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
    // NEW: hashmap index for O(1) lookup by id
    private final CustomHashMap<String, EventRecord> eventsById = new CustomHashMap<>();

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
        eventsById.put(event.id(), event); // NEW
        return event;
    }

    // NEW: instant lookup by id
    public synchronized EventRecord findById(String id) {
        return eventsById.get(id);
    }

    public synchronized boolean removeById(String id) {
        EventRecord event = eventsById.remove(id); // NEW: O(1) check
        if (event == null) {
            return false;
        }
        return events.remove(event);
    }
}
