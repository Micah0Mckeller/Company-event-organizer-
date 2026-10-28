package Event.Java.backend;

import java.util.Objects;

/**
 * Hand-written hash map using separate chaining.
 * put / get / remove / containsKey are O(1) on average, O(n) worst case.
 * Doubles its bucket array when more than 75% full.
 *
 * To swap in java.util.HashMap instead, change the field's type and import;
 * every method used here (put, get, remove, containsKey, size) exists there too.
 */
public class CustomHashMap<K, V> {

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomHashMap() {
        buckets = (Entry<K, V>[]) new Entry[INITIAL_CAPACITY];
    }

    /** Maps a key to a bucket index. */
    private int indexFor(Object key, int length) {
        int h = (key == null) ? 0 : key.hashCode();
        h ^= (h >>> 16); // spread high bits so similar hash codes don't pile up
        return (h & 0x7fffffff) % length;
    }

    /** Adds or replaces a mapping. Returns the old value, or null if the key was new. */
    public V put(K key, V value) {
        int i = indexFor(key, buckets.length);
        for (Entry<K, V> e = buckets[i]; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }
        Entry<K, V> node = new Entry<>(key, value);
        node.next = buckets[i];
        buckets[i] = node;
        size++;
        if (size > buckets.length * LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    /** Returns the value for the key, or null if absent. */
    public V get(Object key) {
        int i = indexFor(key, buckets.length);
        for (Entry<K, V> e = buckets[i]; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                return e.value;
            }
        }
        return null;
    }

    public boolean containsKey(Object key) {
        int i = indexFor(key, buckets.length);
        for (Entry<K, V> e = buckets[i]; e != null; e = e.next) {
            if (Objects.equals(e.key, key)) {
                return true;
            }
        }
        return false;
    }

    /** Removes the mapping and returns its value, or null if the key was absent. */
    public V remove(Object key) {
        int i = indexFor(key, buckets.length);
        Entry<K, V> prev = null;
        for (Entry<K, V> e = buckets[i]; e != null; prev = e, e = e.next) {
            if (Objects.equals(e.key, key)) {
                if (prev == null) {
                    buckets[i] = e.next;
                } else {
                    prev.next = e.next;
                }
                size--;
                return e.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] old = buckets;
        Entry<K, V>[] bigger = (Entry<K, V>[]) new Entry[old.length * 2];
        for (Entry<K, V> head : old) {
            Entry<K, V> e = head;
            while (e != null) {
                Entry<K, V> next = e.next;
                int i = indexFor(e.key, bigger.length);
                e.next = bigger[i];
                bigger[i] = e;
                e = next;
            }
        }
        buckets = bigger;
    }
}
