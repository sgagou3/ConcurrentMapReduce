package comp533.reduce;

import java.util.List;
import java.util.Map;

public interface TokenReducer<K, V> {
	Map<K, V> reduce(List<MapEntry<K, V>> aMapEntryList);
}
