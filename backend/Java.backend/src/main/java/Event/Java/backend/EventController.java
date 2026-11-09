package Event.Java.backend;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:5173")
public class EventController {
    private final EventRepository eventRepository;

    /** Creates the controller with its event storage dependency. */
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /** Returns all events in the order they were submitted. */
    @GetMapping
    public List<EventRecord> getEvents() {
        return eventRepository.findAll();
    }

    /** Returns the event with the requested UUID, or not found when it is absent. */
    @GetMapping("/{id}")
    public ResponseEntity<EventRecord> getEventById(@PathVariable String id) {
        Optional<EventRecord> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Validates and creates an event, returning the created record. */
    @PostMapping
    public ResponseEntity<EventRecord> createEvent(@RequestBody CreateEventRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(eventRepository.add(request));
    }

    /** Deletes an event by ID, returning not found when it does not exist. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        if (!eventRepository.removeById(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
