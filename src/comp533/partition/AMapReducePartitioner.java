package comp533.partition;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReducePartitioner extends AMapReduceTracer implements MapReducePartitioner<String, Integer> {

	@Override
	public int getPartition(final String key, final Integer value, final int numberOfPartitions) {
		final char aFirstChar = key.charAt(0);
		if (!Character.isLetter(aFirstChar)) {
			return 0;
		}
		final char aFirstLowerChar = Character.toLowerCase(aFirstChar);
		final int aMaxPartitionSize = (int) Math.ceil(('z' - 'a' + 1) / (double) numberOfPartitions);
		final int aPartitionIndex = (int) Math.floor((aFirstLowerChar - 'a' + 1) / (double) aMaxPartitionSize);
		tracePartitionAssigned(key, value, aPartitionIndex, numberOfPartitions);
		return aPartitionIndex;
	}

	@Override
	public String toString() {
		return PARTITIONER;
	}

}
