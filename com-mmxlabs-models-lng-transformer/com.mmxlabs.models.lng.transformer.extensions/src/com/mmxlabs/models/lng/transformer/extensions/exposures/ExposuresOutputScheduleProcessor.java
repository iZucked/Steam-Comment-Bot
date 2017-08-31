/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions.exposures;

import javax.inject.Inject;

import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.transformer.IOutputScheduleProcessor;
import com.mmxlabs.models.lng.transformer.util.LNGSchedulerJobUtils;

public class ExposuresOutputScheduleProcessor implements IOutputScheduleProcessor {

	@Inject
	private LNGScenarioModel scenarioModel;

	@Override
	public void process(final Schedule schedule) {
		if (LicenseFeatures.isPermitted("features:exposures")) {

			final EditingDomain domain = LNGSchedulerJobUtils.createLocalEditingDomain();
			Exposures.calculateExposures(scenarioModel, schedule, domain);
		}
	}

}
