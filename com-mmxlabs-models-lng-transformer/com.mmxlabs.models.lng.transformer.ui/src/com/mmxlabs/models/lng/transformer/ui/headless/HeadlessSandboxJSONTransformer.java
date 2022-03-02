/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.headless;

import java.io.File;

public class HeadlessSandboxJSONTransformer extends HeadlessGenericJSONTransformer<HeadlessSandboxJSON.Params, HeadlessSandboxJSON.Metrics, HeadlessSandboxJSON>{

	public HeadlessSandboxJSON createJSONResultObject(String machineType, HeadlessSandboxOptions options, File scenarioFile, int threads) {
		HeadlessSandboxJSON result = createJSONResultObject(HeadlessSandboxJSON.Params.class, HeadlessSandboxJSON.Metrics.class, HeadlessSandboxJSON.class);
		result.setType("sandbox");
		setBasicProperties(result, machineType, scenarioFile.getName(), threads);		
//		result.getParams().setOptioniserProperties(createOptioniserProperties(options));

		return result;		
	}
//	 
}
