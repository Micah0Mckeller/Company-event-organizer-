package Event.Java.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomHashMapTest {

    private CustomHashMap<String, String> map;

    /** Creates a fresh map before each test. */
    @BeforeEach
    void setUp() {
        map = new CustomHashMap<>();
    }

    /** Verifies a new map has no entries and returns expected empty lookups. */
    @Test
    void newMapIsEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("x"));
        assertFalse(map.containsKey("x"));
    }

    /** Verifies inserted values can be retrieved and are reflected in map size. */
    @Test
    void putThenGet() {
        assertNull(map.put("a", "1"));
        assertEquals("1", map.get("a"));
        assertTrue(map.containsKey("a"));
        assertEquals(1, map.size());
    }

    /** Verifies inserting an existing key replaces its value without adding an entry. */
    @Test
    void putSameKeyReplacesValue() {
        map.put("a", "1");
        assertEquals("1", map.put("a", "2"));
        assertEquals("2", map.get("a"));
        assertEquals(1, map.size());
    }

    /** Verifies removing a key returns its value and deletes the entry. */
    @Test
    void removeReturnsValueAndDeletes() {
        map.put("a", "1");
        assertEquals("1", map.remove("a"));
        assertNull(map.get("a"));
        assertEquals(0, map.size());
    }

    /** Verifies removing an absent key leaves the map unchanged. */
    @Test
    void removeMissingReturnsNull() {
        map.put("a", "1");
        assertNull(map.remove("zzz"));
        assertEquals(1, map.size());
    }

    /** Verifies distinct keys with identical hash codes remain independently accessible. */
    @Test
    void collidingKeysAreBothKept() {
        // "Aa" and "BB" have the same hashCode in Java
        assertEquals("Aa".hashCode(), "BB".hashCode());
        map.put("Aa", "first");
        map.put("BB", "second");
        assertEquals("first", map.get("Aa"));
        assertEquals("second", map.get("BB"));
        assertEquals("first", map.remove("Aa"));
        assertEquals("second", map.get("BB"));
    }

    /** Verifies the map retains all entries and values after growing substantially. */
    @Test
    void survivesResizing() {
        for (int i = 0; i < 5000; i++) {
            map.put("id-" + i, "v" + i);
        }
        assertEquals(5000, map.size());
        for (int i = 0; i < 5000; i++) {
            assertEquals("v" + i, map.get("id-" + i));
        }
    }

    /** Verifies a cleared map can be reused for new entries. */
    @Test
    void removeManyThenReuse() {
        for (int i = 0; i < 100; i++) {
            map.put("k" + i, "v" + i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals("v" + i, map.remove("k" + i));
        }
        assertTrue(map.isEmpty());
        map.put("again", "ok");
        assertEquals("ok", map.get("again"));
    }
}
