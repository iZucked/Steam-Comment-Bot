/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
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

public class HeadlessOptimiserOneshotApplication extends HeadlessCloudOptimiserApplication {

	@Override
	protected AbstractJobRunner createRunner() {
		return new OptimisationJobRunner();
	}

	@Override
	protected String getAlgorithmName() {
		return "optimisation";
	}

}
