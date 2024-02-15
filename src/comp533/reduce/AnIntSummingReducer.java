package comp533.reduce;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AnIntSummingReducer extends AMapReduceTracer implements TokenReducer<String, Integer> {
	final String superKey = "ResultKey";

	@Override
	public Map<String, Integer> reduce(final List<MapEntry<String, Integer>> aMapEntryList) {
		final Map<String, Integer> aReturnMap = new HashMap<String, Integer>();
		aReturnMap.put(superKey, 0);

		final Iterator<MapEntry<String, Integer>> iterator = aMapEntryList.iterator();
		while (iterator.hasNext()) {
			final MapEntry<String, Integer> aNextMapEntry = iterator.next();
			final String aKey = aNextMapEntry.getKey();
			final Integer aNextValue = aNextMapEntry.getValue();
			final Integer aCurrentValue = aReturnMap.get(aKey);
			aReturnMap.put(aKey, aNextValue + aCurrentValue);
		}
		traceReduce(aMapEntryList, aReturnMap);
		return aReturnMap;
	}

	@Override
	public String toString() {
		return REDUCER;
	}
}
