package comp533.base;

import comp533.factories.AMapperFactory;
import comp533.mapper.ATokenMapper;
import comp533.mvc.controller.AMapReduceController;
import comp533.mvc.controller.MapReduceController;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.mvc.view.AMapReduceView;
import comp533.mvc.view.MapReduceView;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class ATokenCounter extends AMapReduceTracer implements TokenCounter {
	MapReduceModel aModel = new AMapReduceModel();
	MapReduceView aView = new AMapReduceView();
	MapReduceController aController = new AMapReduceController(aModel);

	public ATokenCounter() {
		aModel.addPropertyChangeListener(aView);
	}

	@Override
	public void processInput() {
		aController.processInput();
	}

	public static void main(final String[] args) {
		final TokenCounter aCounter = new ATokenCounter();
		AMapperFactory.setMapper(new ATokenMapper());
		aCounter.processInput();
	}
}
