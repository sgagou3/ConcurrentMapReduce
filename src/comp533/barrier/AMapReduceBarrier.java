package comp533.barrier;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceBarrier extends AMapReduceTracer implements MapReduceBarrier {
	int totalThreadCount;
	int currentThreadCount = 0;

	public AMapReduceBarrier(final int aThreadCount) {
		totalThreadCount = aThreadCount;
	}

	@Override
	public synchronized void barrier() {
		currentThreadCount++;

		if (totalThreadCount == currentThreadCount) {
			traceNotify();
			notifyAll();
		} else {
			try {
				traceBarrierWaitStart(this, currentThreadCount, totalThreadCount);
				traceWait();
				wait();
				traceBarrierWaitEnd(this, currentThreadCount, totalThreadCount);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		traceBarrierReleaseAll(this, currentThreadCount, totalThreadCount);
		currentThreadCount = 0;
	}

	@Override
	public String toString() {
		return BARRIER;
	}
}
