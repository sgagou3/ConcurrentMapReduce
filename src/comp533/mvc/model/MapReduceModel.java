package comp533.mvc.model;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.MapReduceBarrier;
import comp533.joiner.MapReduceJoiner;
import comp533.reduce.MapEntry;

public interface MapReduceModel {
	String getInputString();

	void setInputString(String aNewInputString);

	void addPropertyChangeListener(PropertyChangeListener aNewListener);

	Map<String, Integer> getResult();

	String toString();
	
	void setRunnableList(List<Runnable>slaveList);

	void setNumThreads(int aNewNumThreadCount);

	int getNumThreads();
	
	List<Thread> getThreads();

	BlockingQueue<MapEntry<String, Integer>> getKeyValueQueue();

	List<LinkedList<MapEntry<String, Integer>>> getReductionQueueList();

	MapReduceJoiner getJoiner();

	MapReduceBarrier getBarrier();
	
	void terminate();
}
