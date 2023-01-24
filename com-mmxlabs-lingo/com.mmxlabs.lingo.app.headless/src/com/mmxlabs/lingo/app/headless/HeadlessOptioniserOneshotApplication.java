/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.app.headless;

import com.mmxlabs.models.lng.transformer.ui.jobrunners.AbstractJobRunner;
import com.mmxlabs.models.lng.transformer.ui.jobrunners.optioniser.OptioniserJobRunner;

/**
 * Headless Optimisation Runner
 * 
 * @author Simon Goodall
 * 
 */

public class HeadlessOptioniserOneshotApplication extends HeadlessCloudOptimiserApplication {

	@Override
	protected AbstractJobRunner createRunner() {
		return new OptioniserJobRunner();
	}

	@Override
	protected String getAlgorithmName() {
		return "optimisation";
	}

}
