package comp533.mapper;

import java.util.ArrayList;
import java.util.List;

import comp533.reduce.AMapEntry;
import comp533.reduce.MapEntry;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AnIntSummingMapper extends AMapReduceTracer implements TokenMapper<String, Integer> {

	@Override
	public List<MapEntry<String, Integer>> map(final List<String> aKeyList) {
		final List<MapEntry<String, Integer>> aReturnValue = new ArrayList<MapEntry<String, Integer>>();
		for (String aKeyValue : aKeyList) {
			aReturnValue.add(new AMapEntry<String, Integer>("ResultKey", Integer.parseInt(aKeyValue)));
		}
		traceMap(aKeyList, aReturnValue);
		return aReturnValue;
	}

	@Override
	public String toString() {
		return INT_SUMMING_MAPPER;
	}
}
