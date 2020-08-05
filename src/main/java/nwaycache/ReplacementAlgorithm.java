package nwaycache;

import java.util.Comparator;
public interface ReplacementAlgorithm<K,V,M> extends Comparator<CacheElement<K,V,M>> {
	void onPut(CacheElement<K,V,M> element);
	void onGet(CacheElement<K,V,M> element);
}
