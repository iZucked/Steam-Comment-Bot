/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optimisation.OptimisationJobRunner;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessCloudApplication extends HeadlessCloudOptimiserApplication {


	@Override
	protected AbstractJobRunner createRunner() {
		return new OptimisationJobRunner();
	}

	@Override
	protected String getAlgorithmName() {
		return "optimisation";
	}
}
