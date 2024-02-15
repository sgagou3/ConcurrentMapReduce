package comp533.mvc.controller;

public interface MapReduceController {
	void processInput();

	String toString();
	
	Runnable getSlave(int anIndex);
}
