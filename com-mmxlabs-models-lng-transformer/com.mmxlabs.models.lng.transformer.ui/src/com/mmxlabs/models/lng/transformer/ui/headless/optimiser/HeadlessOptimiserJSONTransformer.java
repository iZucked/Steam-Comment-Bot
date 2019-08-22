/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.util.LinkedList;
import java.util.List;

public class HeadlessOptimiserJSONTransformer {

	public static HeadlessOptimiserJSON createJSONResultObject() {
		HeadlessOptimiserJSON result = new HeadlessOptimiserJSON();
		result.setType("optimiser");
		addMetaDataToResult(result);
//		addParamsToResult(result);
		addMetricsToResult(result);
//		addOptioniserParamsToResult(result);
		return result;
	}

	private static void addMetricsToResult(HeadlessOptimiserJSON result) {
		HeadlessOptimiserJSON.Metrics metrics = new HeadlessOptimiserJSON.Metrics();
		result.setMetrics(metrics);				
	}

//	public static void addRunResult(SlotInsertionOptimiserLogger logger, HeadlessOptimiserJSON result) {
//		HeadlessOptimiserJSON.Metrics metrics = new HeadlessOptimiserJSON.Metrics();
//		metrics.setRuntime(logger.getRuntime());
//
//		HeadlessOptimiserJSON.OptioniserMetrics om = new HeadlessOptimiserJSON.OptioniserMetrics();
//		om.setSolutionsFound(logger.getSolutionsFound());
//		// om.setSolutionsReturned(0);
//
//		metrics.setOptioniserMetrics(om);
//
//		List<HeadlessOptimiserJSON.Stage> stages = new LinkedList<>();
//		for (String stageName : logger.getStages()) {
//			HeadlessOptimiserJSON.Stage stage = new HeadlessOptimiserJSON.Stage();
//			stage.setStage(stageName);
//			stage.setRuntime(logger.getRuntime(stageName));
//			stages.add(stage);
//		}
//
//		metrics.setStages(stages);
//
//		result.addRun(metrics);
//	}
	
//	private static void addOptioniserParamsToResult(HeadlessOptimiserJSON result) {
//		HeadlessOptimiserJSON.Params params = addParamsToResult();
//
//		HeadlessOptimiserJSON.OptioniserProperties op = new HeadlessOptimiserJSON.OptioniserProperties();
//		op.setLoadIds(new String[0]);
//		op.setDischargeIds(new String[0]);
//		op.setEventIds(new String[0]);
//		op.setIterations(0);
//
//		params.setOptioniserProperties(op);
//
//		result.setParams(params);
//	}

	private static HeadlessOptimiserJSON.Params addParamsToResult() {
		HeadlessOptimiserJSON.Params params = new HeadlessOptimiserJSON.Params();
		params.setCores(0);
//		result.setParams(params);
		return params;
	}

	private static void addMetaDataToResult(HeadlessOptimiserJSON result) {
		HeadlessOptimiserJSON.Meta meta = new HeadlessOptimiserJSON.Meta();
		meta.setMachineType("");
		meta.setVersion("");
		meta.setScenario("");
		meta.setClient("");

		result.setMeta(meta);
	}
}
