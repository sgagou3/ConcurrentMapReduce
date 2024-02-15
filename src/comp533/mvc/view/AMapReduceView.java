package comp533.mvc.view;

import java.beans.PropertyChangeEvent;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapReduceView extends AMapReduceTracer implements MapReduceView {
	@Override
	public String toString() {
		return VIEW;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent aPropertyChangeEvent) {
		tracePropertyChange(aPropertyChangeEvent);
	}
}
