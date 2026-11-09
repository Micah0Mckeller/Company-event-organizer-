
package Event.Java.backend;

import java.util.LinkedHashMap;

/**
 * A standard Java hash map that retains insertion order for event queue listings.
 */
public class CustomHashMap<K, V> extends LinkedHashMap<K, V> {
    /** Identifies this map's serialized form for Java serialization. */
    private static final long serialVersionUID = 1L;
}