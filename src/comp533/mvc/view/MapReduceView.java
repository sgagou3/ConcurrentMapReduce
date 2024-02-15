package comp533.mvc.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventListener;

public interface MapReduceView extends PropertyChangeListener, EventListener {
	void propertyChange(PropertyChangeEvent aPropertyChangeEvent);
}
