/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.exposures;

import javax.inject.Inject;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresOutputScheduleProcessor implements IOutputScheduleProcessor {

	@Inject 
	private IScenarioDataProvider scenarioDataProvider;

	@Override
	public void process(final Schedule schedule) {
		if (LicenseFeatures.isPermitted("features:exposures")) {
			Exposures.calculateExposures(scenarioDataProvider, schedule);
		}
	}

}
