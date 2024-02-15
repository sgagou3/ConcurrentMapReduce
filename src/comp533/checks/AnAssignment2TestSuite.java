package comp533.checks;

import grader.basics.execution.BasicProjectExecution;
import gradingTools.comp533s24.assignment2.S24Assignment2Suite;
import trace.grader.basics.GraderBasicsTraceUtility;

public class AnAssignment2TestSuite {
	static final int MAX_PRINTED_TRACES = 600;
	static final int MAX_TRACES = 2000;
	static final int WAIT_COUNT = 30;

	public static void main(final String[] args) {
		// if you set this to false, grader steps will not be traced
		GraderBasicsTraceUtility.setTracerShowInfo(true);
		// if you set this to false, all grader steps will be traced,
		// not just the ones that failed
		GraderBasicsTraceUtility.setBufferTracedMessages(true);
		// Change this number if a test trace gets longer than 600 and is clipped
		GraderBasicsTraceUtility.setMaxPrintedTraces(MAX_PRINTED_TRACES);
		// Change this number if all traces together are longer than 2000
		GraderBasicsTraceUtility.setMaxTraces(MAX_TRACES);
		// Change this number if your process times out prematurely
		BasicProjectExecution.setProcessTimeOut(WAIT_COUNT);
		// You need to always call such a method
		S24Assignment2Suite.main(args);
	}
}
