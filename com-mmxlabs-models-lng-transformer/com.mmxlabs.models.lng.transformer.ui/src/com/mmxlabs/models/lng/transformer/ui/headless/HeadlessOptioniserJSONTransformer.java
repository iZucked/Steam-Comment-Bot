/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessOptioniserRunner.Options;
import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public class HeadlessOptioniserJSONTransformer extends HeadlessGenericJSONTransformer<HeadlessOptioniserJSON.Params, HeadlessOptioniserJSON.Metrics, HeadlessOptioniserJSON>{

	public HeadlessOptioniserJSON createJSONResultObject(String machineType, Options options, File scenarioFile, int threads) {
		HeadlessOptioniserJSON result = createJSONResultObject(HeadlessOptioniserJSON.Params.class, HeadlessOptioniserJSON.Metrics.class, HeadlessOptioniserJSON.class);
		result.setType("optioniser");
		setBasicProperties(result, machineType, scenarioFile.getName(), threads);		
		result.getParams().setOptioniserProperties(createOptioniserProperties(options));

		return result;		
	}
	
	public void addRunResult(int startTry, SlotInsertionOptimiserLogger logger, HeadlessOptioniserJSON result) {
		HeadlessOptioniserJSON.Metrics metrics = new HeadlessOptioniserJSON.Metrics();
		metrics.setSeed(startTry);
		metrics.setRuntime(logger.getRuntime());

		HeadlessOptioniserJSON.OptioniserMetrics om = new HeadlessOptioniserJSON.OptioniserMetrics();
		om.setSolutionsFound(logger.getSolutionsFound());

		metrics.setOptioniserMetrics(om);

		List<HeadlessOptioniserJSON.Stage> stages = new LinkedList<>();
		for (String stageName : logger.getStages()) {
			HeadlessOptioniserJSON.Stage stage = new HeadlessOptioniserJSON.Stage();
			stage.setStage(stageName);
			stage.setRuntime(logger.getRuntime(stageName));
			stages.add(stage);
		}

		metrics.setStages(stages);
		metrics.setMemoryUsage(logger.getIterationHeapUsages());

		result.setMetrics(metrics);
	}
	
	private HeadlessOptioniserJSON.OptioniserProperties createOptioniserProperties(Options options) {
		HeadlessOptioniserJSON.OptioniserProperties op = new HeadlessOptioniserJSON.OptioniserProperties();

		op.setLoadIds(options.loadIds.toArray(new String[0]));
		op.setDischargeIds(options.dischargeIds.toArray(new String[0]));
		op.setEventIds(options.eventsIds.toArray(new String[0]));
		op.setIterations(options.iterations);
		
		return op;
	}

}
