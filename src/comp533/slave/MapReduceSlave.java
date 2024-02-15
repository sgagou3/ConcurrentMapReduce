package comp533.slave;

public interface MapReduceSlave extends Runnable {
	void waitSlave();
	void notifySlave();
}
