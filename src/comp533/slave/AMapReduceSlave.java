package comp533.slave;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import comp533.barrier.MapReduceBarrier;
import comp533.factories.APartitionerFactory;
import comp533.factories.AReducerFactory;
import comp533.joiner.MapReduceJoiner;
import comp533.mvc.model.MapReduceModel;
import comp533.partition.MapReducePartitioner;
import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceSlave extends AMapReduceTracer implements MapReduceSlave {
	MapReduceModel model;
	MapReduceBarrier barrier;
	MapReduceJoiner joiner;

	int slaveNumber;

	MapReducePartitioner<String, Integer> partitioner;
	TokenReducer<String, Integer> reducer;
	List<LinkedList<MapEntry<String, Integer>>> aReductionQueueList;

	public AMapReduceSlave(final MapReduceModel aModel, final int aSlaveNumber) {
		model = aModel;
		slaveNumber = aSlaveNumber;

		partitioner = APartitionerFactory.getPartitioner();
		reducer = AReducerFactory.getReducer();
		aReductionQueueList = model.getReductionQueueList();
		barrier = null;
		joiner = null;
	}

	private List<MapEntry<String, Integer>> getInput() {
		final List<MapEntry<String, Integer>> aConsumedList = new ArrayList<MapEntry<String, Integer>>();
		final BlockingQueue<MapEntry<String, Integer>> aBlockingQueue = model.getKeyValueQueue();
		MapEntry<String, Integer> aNextElement;
		try {
			while (true) {
				aNextElement = aBlockingQueue.take();
				if (aNextElement == null) {
					traceDequeueRequest(aBlockingQueue);
					continue;
				} else if (aNextElement.getKey() != null && aNextElement.getValue() != null) {
					traceDequeueRequest(aBlockingQueue);
					aConsumedList.add(aNextElement);
					traceDequeue(aNextElement);
				} else {
					traceDequeueRequest(aBlockingQueue);
					traceDequeue(aNextElement);
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return aConsumedList;
	}

	private void doFirstReduction(final Map<String, Integer> aReducedMap) {
		synchronized (aReductionQueueList) {
			for (final Map.Entry<String, Integer> aMapEntry : aReducedMap.entrySet()) {
				final String aKey = aMapEntry.getKey();
				final Integer aValue = aMapEntry.getValue();
				final int aPartitionIndex = partitioner.getPartition(aKey, aValue, model.getNumThreads());
				tracePartitionAssigned(aKey, aValue, aPartitionIndex, model.getNumThreads());
				aReductionQueueList.get(aPartitionIndex).add(new AMapEntry<String, Integer>(aKey, aValue));
			}
		}
	}

	private void doSecondReduction() {
		synchronized (aReductionQueueList) {
			final LinkedList<MapEntry<String, Integer>> aFinalReductionList = aReductionQueueList.get(slaveNumber);
			traceSplitAfterBarrier(slaveNumber, aReductionQueueList.get(slaveNumber));
			final Map<String, Integer> aFinalReductionMap = reducer.reduce(aFinalReductionList);
			aReductionQueueList.get(slaveNumber).clear();
			for (final Map.Entry<String, Integer> aMapEntry : aFinalReductionMap.entrySet()) {
				aReductionQueueList.get(slaveNumber)
						.add(new AMapEntry<String, Integer>(aMapEntry.getKey(), aMapEntry.getValue()));
			}
		}
	}

	@Override
	public void run() {
		barrier = model.getBarrier();
		joiner = model.getJoiner();
		while (!Thread.currentThread().isInterrupted()) {
			final List<MapEntry<String, Integer>> aConsumedList = getInput();

			final Map<String, Integer> aReducedMap = reducer.reduce(aConsumedList);

			doFirstReduction(aReducedMap);

			barrier.barrier();

			doSecondReduction();

			joiner.finished();

			waitSlave();
		}
	}

	@Override
	public synchronized void waitSlave() {
		try {
			traceWait();
			wait();
		} catch (InterruptedException e) {
			traceQuit();
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public synchronized void notifySlave() {
		traceNotify();
		notify();
	}

	@Override
	public String toString() {
		return SLAVE;
	}
}
