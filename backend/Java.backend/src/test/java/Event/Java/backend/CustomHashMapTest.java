package Event.Java.backend;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CustomHashMapTest {

    private CustomHashMap<String, String> map;

    @BeforeEach
    void setUp() {
        map = new CustomHashMap<>();
    }

    @Test
    void newMapIsEmpty() {
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("x"));
        assertFalse(map.containsKey("x"));
    }

    @Test
    void putThenGet() {
        assertNull(map.put("a", "1"));
        assertEquals("1", map.get("a"));
        assertTrue(map.containsKey("a"));
        assertEquals(1, map.size());
    }

    @Test
    void putSameKeyReplacesValue() {
        map.put("a", "1");
        assertEquals("1", map.put("a", "2"));
        assertEquals("2", map.get("a"));
        assertEquals(1, map.size());
    }

    @Test
    void removeReturnsValueAndDeletes() {
        map.put("a", "1");
        assertEquals("1", map.remove("a"));
        assertNull(map.get("a"));
        assertEquals(0, map.size());
    }

    @Test
    void removeMissingReturnsNull() {
        map.put("a", "1");
        assertNull(map.remove("zzz"));
        assertEquals(1, map.size());
    }

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
