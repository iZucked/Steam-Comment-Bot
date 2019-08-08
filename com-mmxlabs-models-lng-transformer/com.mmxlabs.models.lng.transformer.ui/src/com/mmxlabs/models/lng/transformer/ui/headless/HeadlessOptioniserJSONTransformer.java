/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.util.LinkedList;
import java.util.List;

import com.mmxlabs.scheduler.optimiser.insertion.SlotInsertionOptimiserLogger;

public class HeadlessOptioniserJSONTransformer {

	public static HeadlessOptioniserJSON createJSONResultObject() {
		HeadlessOptioniserJSON result = new HeadlessOptioniserJSON();
		result.setType("optioniser");
		addMetaDataToResult(result);
		addOptioniserParamsToResult(result);
		return result;
	}

	public static void addRunResult(SlotInsertionOptimiserLogger logger, HeadlessOptioniserJSON result) {
		HeadlessOptioniserJSON.Metrics metrics = new HeadlessOptioniserJSON.Metrics();
		metrics.setRuntime(logger.getRuntime());

		HeadlessOptioniserJSON.OptioniserMetrics om = new HeadlessOptioniserJSON.OptioniserMetrics();
		om.setSolutionsFound(logger.getSolutionsFound());
		// om.setSolutionsReturned(0);

		metrics.setOptioniserMetrics(om);

		List<HeadlessOptioniserJSON.Stage> stages = new LinkedList<>();
		for (String stageName : logger.getStages()) {
			HeadlessOptioniserJSON.Stage stage = new HeadlessOptioniserJSON.Stage();
			stage.setStage(stageName);
			stage.setRuntime(logger.getRuntime(stageName));
			stages.add(stage);
		}

		metrics.setStages(stages);

		result.addRun(metrics);
	}
	
	private static void addOptioniserParamsToResult(HeadlessOptioniserJSON result) {
		HeadlessOptioniserJSON.Params params = addParamsToResult();

		HeadlessOptioniserJSON.OptioniserProperties op = new HeadlessOptioniserJSON.OptioniserProperties();
		op.setLoadIds(new String[0]);
		op.setDischargeIds(new String[0]);
		op.setEventIds(new String[0]);
		op.setIterations(0);

		params.setOptioniserProperties(op);

		result.setParams(params);
	}

	private static HeadlessOptioniserJSON.Params addParamsToResult() {
		HeadlessOptioniserJSON.Params params = new HeadlessOptioniserJSON.Params();
		params.setCores(0);
		return params;
	}

	private static void addMetaDataToResult(HeadlessOptioniserJSON result) {
		HeadlessOptioniserJSON.Meta meta = new HeadlessOptioniserJSON.Meta();
		meta.setMachineType("");
		meta.setVersion("");
		meta.setScenario("");
		meta.setClient("");

		result.setMeta(meta);
	}
}
