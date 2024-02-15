package comp533.mvc.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.AMapReduceBarrier;
import comp533.barrier.MapReduceBarrier;
import comp533.factories.AMapperFactory;
import comp533.joiner.AMapReduceJoiner;
import comp533.joiner.MapReduceJoiner;
import comp533.mapper.TokenMapper;
import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceModel extends AMapReduceTracer implements MapReduceModel {
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	String inputString;

	Map<String, Integer> mapReduceResult;

	List<Thread> threadList;
	List<Runnable> slaveList;
	int threadCount;

	BlockingQueue<MapEntry<String, Integer>> blockingQueue = new ArrayBlockingQueue<MapEntry<String, Integer>>(
			BUFFER_SIZE);
	List<LinkedList<MapEntry<String, Integer>>> reductionList = new ArrayList<LinkedList<MapEntry<String, Integer>>>();

	MapReduceJoiner joiner;
	MapReduceBarrier barrier;

	@Override
	public String getInputString() {
		return inputString;
	}

	@Override
	public String toString() {
		return MODEL;
	}

	@Override
	public void setInputString(final String aNewInputString) {
		final String anOldInputString = inputString;

		inputString = aNewInputString;

		final PropertyChangeEvent anInputPropertyChangeEvent = new PropertyChangeEvent(this, "InputString",
				anOldInputString, aNewInputString);

		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEvent);

		final String[] aSplitInputStringArray = inputString.split(" ");
		final List<String> aSplitInputArrayList = Arrays.asList(aSplitInputStringArray);

		final TokenMapper<String, Integer> mapper = AMapperFactory.getMapper();
		final List<MapEntry<String, Integer>> aMappingResult = mapper.map(aSplitInputArrayList);

		final int aSplitInputArrayListLength = aMappingResult.size();

		for (int aCounter = 0; aCounter < aSplitInputArrayListLength; aCounter++) {
			try {
				traceEnqueueRequest(aMappingResult.get(aCounter));
				blockingQueue.put(aMappingResult.get(aCounter));
				traceEnqueue(aMappingResult.get(aCounter));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		addBlockersToQueue();

		joiner.join();

		traceJoinerRelease(this, threadCount, threadCount);

		final Map<String, Integer> aReducingResult = new HashMap<String, Integer>();

		completeReduction(aReducingResult);

		final PropertyChangeEvent aReductionPropertyChangeEvent = new PropertyChangeEvent(this, "Result", null,
				aReducingResult.toString());
		propertyChangeSupport.firePropertyChange(aReductionPropertyChangeEvent);

		mapReduceResult = aReducingResult;

		clearReductionQueueList();
	}

	private void addBlockersToQueue() {
		for (int aCounter = 0; aCounter < threadCount; aCounter++) {
			try {
				traceEnqueueRequest(new AMapEntry<String, Integer>(null, null));
				blockingQueue.put(new AMapEntry<String, Integer>(null, null));
				traceEnqueue(new AMapEntry<String, Integer>(null, null));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void completeReduction(final Map<String, Integer> aReducingResult) {
		for (final LinkedList<MapEntry<String, Integer>> aNextReductionList : reductionList) {
			placeReductionResult(aNextReductionList,aReducingResult);
			traceAddedToMap(aReducingResult, aNextReductionList);
		}
	}
	
	private void placeReductionResult(final LinkedList<MapEntry<String, Integer>> aNextReductionList,final Map<String, Integer> aReducingResult) {
		for (MapEntry<String, Integer> aNextReduction : aNextReductionList) {
			aReducingResult.put(aNextReduction.getKey(), aNextReduction.getValue());
		}
	}

	private void clearReductionQueueList() {
		for (LinkedList<MapEntry<String, Integer>> aReductionQueue : reductionList) {
			aReductionQueue.clear();
		}
	}

	@Override
	public void addPropertyChangeListener(final PropertyChangeListener aNewListener) {
		propertyChangeSupport.addPropertyChangeListener(aNewListener);
	}

	@Override
	public Map<String, Integer> getResult() {
		return mapReduceResult;
	}

	@Override
	public void setNumThreads(final int aNewNumThreadCount) {
		final List<Thread> aNewThreadList = new ArrayList<Thread>();

		final PropertyChangeEvent anInputPropertyChangeEvent = new PropertyChangeEvent(this, "NumThreads", threadCount,
				aNewNumThreadCount);
		threadCount = aNewNumThreadCount;
		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEvent);

		joiner = new AMapReduceJoiner(threadCount);
		traceJoinerCreated(this, threadCount);
		barrier = new AMapReduceBarrier(threadCount);
		traceBarrierCreated(this, threadCount);

		for (int aCounter = 0; aCounter < threadCount; aCounter++) {
			final Thread aThread = new Thread(slaveList.get(aCounter));
			aThread.setName("Slave" + aCounter);
			aNewThreadList.add(aThread);
			reductionList.add(new LinkedList<MapEntry<String, Integer>>());
			aThread.start();
		}

		final PropertyChangeEvent anInputPropertyChangeEventForThreadList = new PropertyChangeEvent(this, "Threads",
				threadList, aNewThreadList);
		propertyChangeSupport.firePropertyChange(anInputPropertyChangeEventForThreadList);
		threadList = aNewThreadList;
	}

	@Override
	public int getNumThreads() {
		return threadCount;
	}

	@Override
	public List<Thread> getThreads() {
		return threadList;
	}

	@Override
	public BlockingQueue<MapEntry<String, Integer>> getKeyValueQueue() {
		return blockingQueue;
	}

	@Override
	public List<LinkedList<MapEntry<String, Integer>>> getReductionQueueList() {
		return reductionList;
	}

	@Override
	public MapReduceJoiner getJoiner() {
		return joiner;
	}

	@Override
	public MapReduceBarrier getBarrier() {
		return barrier;
	}

	@Override
	public void terminate() {
		for (Thread aThread : threadList) {
			aThread.interrupt();
		}
	}

	@Override
	public void setRunnableList(final List<Runnable> aSlaveList) {
		slaveList = aSlaveList;

	}
}
