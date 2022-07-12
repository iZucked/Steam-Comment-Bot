/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless.optimiser;

import java.io.File;

import org.json.simple.JSONObject;

import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSON;
import com.mmxlabs.models.lng.transformer.ui.headless.HeadlessGenericJSONTransformer;

public class HeadlessOptimiserJSONTransformer extends HeadlessGenericJSONTransformer<HeadlessGenericJSON.Params, HeadlessOptimiserJSON.Metrics, HeadlessOptimiserJSON> {

	public HeadlessOptimiserJSON createJSONResultObject(JSONObject machineType, File scenarioFile, int threads) {
		HeadlessOptimiserJSON result = createJSONResultObject(HeadlessGenericJSON.Params.class, HeadlessOptimiserJSON.Metrics.class, HeadlessOptimiserJSON.class);
		result.setType("optimisation");
		setBasicProperties(result, machineType, scenarioFile.getName(), threads);

		return result;
	}

	public HeadlessOptimiserJSON createJSONResultObject() {
		HeadlessOptimiserJSON result = createJSONResultObject(HeadlessGenericJSON.Params.class, HeadlessOptimiserJSON.Metrics.class, HeadlessOptimiserJSON.class);
		result.setType("optimisation");
		// setBasicProperties(result, machineType, scenarioFile.getName(), threads);

		return result;
	}

}
