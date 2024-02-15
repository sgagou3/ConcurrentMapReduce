package comp533.mapper;

import java.util.List;

import comp533.reduce.MapEntry;

public interface TokenMapper<K, V> {
	List<MapEntry<K, V>> map(List<String> aKeyList);
}
