package comp533.factories;

import comp533.reduce.ATokenReducer;
import comp533.reduce.TokenReducer;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AReducerFactory extends AMapReduceTracer {
	static TokenReducer<String,Integer> reducer = new ATokenReducer();

	public static TokenReducer<String,Integer> getReducer() {
		return reducer;
	}

	public static void setReducer(final TokenReducer<String,Integer> aNewReducer) {
		traceSingletonChange(AReducerFactory.class, aNewReducer);
		reducer = aNewReducer;
	}
}
