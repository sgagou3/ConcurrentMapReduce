package comp533.partition;

public interface MapReducePartitioner<K,V> {
	int getPartition(K key,V value, int numberOfPartitions);
}
