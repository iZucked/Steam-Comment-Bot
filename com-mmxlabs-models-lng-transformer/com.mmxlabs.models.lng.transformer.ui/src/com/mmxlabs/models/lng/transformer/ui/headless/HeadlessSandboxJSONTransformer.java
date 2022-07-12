/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;

import org.eclipse.jdt.annotation.Nullable;
import org.json.simple.JSONObject;

import com.mmxlabs.models.lng.transformer.ui.jobrunners.sandbox.SandboxSettings;

public class HeadlessSandboxJSONTransformer extends HeadlessGenericJSONTransformer<HeadlessSandboxJSON.Params, HeadlessSandboxJSON.Metrics, HeadlessSandboxJSON> {

	public HeadlessSandboxJSON createJSONResultObject(JSONObject machineType, SandboxSettings options, File scenarioFile, int threads) {
		HeadlessSandboxJSON result = createJSONResultObject(HeadlessSandboxJSON.Params.class, HeadlessSandboxJSON.Metrics.class, HeadlessSandboxJSON.class);
		result.setType("sandbox");
		setBasicProperties(result, machineType, scenarioFile.getName(), threads);
		// result.getParams().setOptioniserProperties(createOptioniserProperties(options));

		return result;
	}
	//

	public HeadlessSandboxJSON createJSONResultObject(@Nullable SandboxSettings sandboxSettings) {
		HeadlessSandboxJSON result = createJSONResultObject(HeadlessSandboxJSON.Params.class, HeadlessSandboxJSON.Metrics.class, HeadlessSandboxJSON.class);
		result.setType("sandbox");
		return result;
	}
}
