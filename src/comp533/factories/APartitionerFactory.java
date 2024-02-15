package comp533.factories;

import comp533.partition.AMapReducePartitioner;
import comp533.partition.MapReducePartitioner;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class APartitionerFactory extends AMapReduceTracer {
	static MapReducePartitioner<String, Integer> partitioner = new AMapReducePartitioner();

	public static void setPartitioner(final MapReducePartitioner<String, Integer> aNewPartitioner) {
		traceSingletonChange(APartitionerFactory.class, aNewPartitioner);
		partitioner = aNewPartitioner;
	}

	public static MapReducePartitioner<String, Integer> getPartitioner() {
		return partitioner;
	}
}
