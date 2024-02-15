package comp533.joiner;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceJoiner extends AMapReduceTracer implements MapReduceJoiner {
	int threadCount;
	int currentThreadCount = 0;

	public AMapReduceJoiner(final int aThreadCount) {
		threadCount = aThreadCount;
	}

	@Override
	public synchronized void finished() {
		currentThreadCount++;

		traceJoinerFinishedTask(this, threadCount, currentThreadCount);

		if (currentThreadCount == threadCount) {
			traceNotify();
			notify();
			traceJoinerRelease(this, currentThreadCount, threadCount);
		}
	}

	@Override
	public synchronized void join() {
		if (currentThreadCount < threadCount) {
			try {
				traceJoinerWaitStart(this, threadCount, currentThreadCount);
				traceWait();
				wait();
				traceJoinerWaitEnd(this, threadCount, currentThreadCount);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		currentThreadCount = 0;
	}

	@Override
	public String toString() {
		return JOINER;
	}
}
