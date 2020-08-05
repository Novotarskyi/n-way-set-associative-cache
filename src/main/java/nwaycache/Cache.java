package nwaycache;

/**
 * An interface for a cache
 */
public interface Cache<K, V, M> {
    V get(final K key);
    void put(final K key, final V value);
}
