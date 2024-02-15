package comp533.reduce;

public interface MapEntry<K, V> {
	K getKey();
	
	V getValue();

	void setValue(V aNewValue);
}
