package comp533.reduce;

public class AMapEntry<K, V> implements MapEntry<K, V> {
	K key;
	V value;

	public AMapEntry(final K aKey, final V aValue) {
		key = aKey;
		value = aValue;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return value;
	}

	@Override
	public void setValue(final V aNewValue) {
		value = aNewValue;
	}

	@Override
	public String toString() {
		if(key==null&&value==null) {
			return "(null,null)";
		}
		return "(" + key.toString() + "," + value.toString() + ")";
	}
}
