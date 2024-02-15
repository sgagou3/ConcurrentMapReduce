package comp533.summation;

import comp533.factories.AMapperFactory;
import comp533.mapper.AnIntSummingMapper;
import comp533.mvc.controller.AMapReduceController;
import comp533.mvc.controller.MapReduceController;
import comp533.mvc.model.AMapReduceModel;
import comp533.mvc.model.MapReduceModel;
import comp533.mvc.view.AMapReduceView;
import comp533.mvc.view.MapReduceView;
import gradingTools.comp533s19.assignment0.AMapReduceTracer;

public class AnIntSummer extends AMapReduceTracer implements IntSummer {
	MapReduceModel aModel = new AMapReduceModel();
	MapReduceController aController = new AMapReduceController(aModel);
	MapReduceView aView = new AMapReduceView();
	
	public AnIntSummer() {
		aModel.addPropertyChangeListener(aView);
	}

	@Override
	public void processInput() {
		aController.processInput();
	}

	public static void main(final String[] args) {
		final IntSummer aSummer = new AnIntSummer();
		AMapperFactory.setMapper(new AnIntSummingMapper());
		aSummer.processInput();
	}

}
