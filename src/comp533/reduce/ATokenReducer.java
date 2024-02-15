package comp533.reduce;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class ATokenReducer extends AMapReduceTracer implements TokenReducer<String, Integer> {
	
	@Override
	public Map<String, Integer> reduce(final List<MapEntry<String, Integer>> aMapEntryList) {
		final Map<String, Integer> aReturnMap = new HashMap<String, Integer>();
		final Iterator<MapEntry<String, Integer>> iterator = aMapEntryList.iterator();
		while (iterator.hasNext()) {
			final MapEntry<String, Integer> aNextMapEntry = iterator.next();
			final String aKey = aNextMapEntry.getKey();
			final Integer aValue =aNextMapEntry.getValue();
			if (!aReturnMap.containsKey(aKey)) {
				aReturnMap.put(aKey, aValue);
			} else {
				final Integer aCurrentValue = aReturnMap.get(aKey);
				aReturnMap.put(aKey, aValue + aCurrentValue);
			}
		}
		traceReduce(aMapEntryList, aReturnMap);
		return aReturnMap;
	}

	@Override
	public String toString() {
		return REDUCER;
	}
}
