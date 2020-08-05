package nwaycache;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class NWaySetAssociativeCache<K, V, M> implements Cache<K, V, M> {

    private final List<CacheElement<K, V, M>>[] cacheContents;

    private final int setSize;
    private final int entrySize;
    private final ReplacementAlgorithm<K, V, M> replacementAlgorithm;


    public NWaySetAssociativeCache(int setSize, int entrySize, ReplacementAlgorithm<K, V, M> replacementAlgorithm) {
        this.setSize = setSize;
        this.entrySize = entrySize;
        this.replacementAlgorithm = replacementAlgorithm;
        this.cacheContents = (List<CacheElement<K, V, M>>[])Array.newInstance(List.class, this.setSize);
    }

    public final V get(K key) {
        List<CacheElement<K, V, M>> searchSet = this.getSet(key);
        V data = null;
        for (CacheElement<K, V, M> setElement : searchSet) {
            if (setElement.getKey().equals(key)) {
            	replacementAlgorithm.onGet(setElement);
                data = setElement.getData();
            }
        }
        return data;
    }

    public final void put(K key, V value) {
        CacheElement<K, V, M> evictionCandidate = null;
        
        List<CacheElement<K, V, M>> searchSet = this.getSet(key);

        for (CacheElement<K, V, M> setElement : searchSet) {
        	// Only really need to evict if the set is full
        	if (searchSet.size() == this.entrySize) {
        		evictionCandidate = chooseBetterEvictionCandidate(evictionCandidate, setElement);
        	}
            if (setElement.getKey().equals(key)) {
            	setElement.setKeyValue(key, value);
            	replacementAlgorithm.onPut(setElement);
                return;
            }
        }
        if (this.entrySize > searchSet.size()) {
        	CacheElement<K, V, M> newElement = new CacheElement<>(key, value);
        	replacementAlgorithm.onPut(newElement);
            searchSet.add(newElement);
            return;
        }
        if (evictionCandidate != null) {
            evictionCandidate.setKeyValue(key, value);
            replacementAlgorithm.onPut(evictionCandidate);
        }
    }

    private CacheElement<K, V, M> chooseBetterEvictionCandidate(CacheElement<K, V, M> current, CacheElement<K, V, M> candidate) {
    	if (current == null) {
    		return candidate;
    	}

        if (replacementAlgorithm.compare(current, candidate) > 0) {
            return candidate;
        }
        return current;
    }

    private List<CacheElement<K, V, M>> getSet(K key) {
        int setIndex = Math.floorMod(key.hashCode(), this.setSize);

        if (this.cacheContents[setIndex] == null) {
            this.cacheContents[setIndex] = new ArrayList<>(this.entrySize);
        }

        return this.cacheContents[setIndex];
    }

    public List<CacheElement<K, V, M>>[] getCacheContents() {
        return cacheContents;
    }
}

