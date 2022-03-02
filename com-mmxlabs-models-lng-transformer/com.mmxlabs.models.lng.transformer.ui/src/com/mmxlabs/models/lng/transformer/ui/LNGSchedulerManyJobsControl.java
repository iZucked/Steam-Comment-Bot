/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.rcp.common.ecore.EMFCopier;

public class LNGSchedulerManyJobsControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerManyJobsControl(final LNGRunAllSimilarityJobDescriptor jobDescriptor) throws Exception {

		super(jobDescriptor, (originalPlan, factory) -> {
			int numCopies = 1;
			for (int i = 0; i < numCopies; ++i) {
				final OptimisationPlan optimisationPlan = EMFCopier.copy(originalPlan);
				String name = String.format("Job %02d", i);
				// optimiserSettings.setSeed(i);
				factory.accept(name, optimisationPlan);
			}
		});
	}
}
