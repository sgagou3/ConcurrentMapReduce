package comp533.factories;

import comp533.mapper.ATokenMapper;
import comp533.mapper.TokenMapper;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AMapperFactory extends AMapReduceTracer {
	static TokenMapper<String, Integer> mapper=new ATokenMapper();;

	public static void setMapper(final TokenMapper<String, Integer> aNewMapper) {
		traceSingletonChange(AMapperFactory.class, aNewMapper);
		mapper = aNewMapper;
	}

	public static TokenMapper<String, Integer> getMapper() {
		return mapper;
	}
}
