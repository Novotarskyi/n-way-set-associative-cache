package nwaycache;

public class CacheElement<K, V, M> {
    
    private K key;
    private V data;
    private M metadata;

    public CacheElement(K key, V data) {
        this.key = key;
        this.data = data;
    }

    public K getKey() {
        return this.key;
    }

    public V getData() {
        return this.data;
    }

    public M getMetadata() {
        return this.metadata;
    }

    public void setMetadata(M metadata) {
        this.metadata = metadata;
    }

    public void setKeyValue(K key, V value) {
        this.key = key;
        this.data = value;
    }
}
