/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

public class LNGSchedulerRunMultipleSeedsJobControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerRunMultipleSeedsJobControl(final LNGRunMultipleSeedsJobDescriptor jobDescriptor) throws Exception {
		super(jobDescriptor, (originalPlan, factory) -> {
			// for (int i = 0; i < 20; ++i) {
			// final OptimisationPlan optimisationPlan = EcoreUtil.copy(originalPlan);
			// String name = "Seed-" + i;
			// optimisationPlan.setSeed(i);
			// factory.accept(name, optimisationPlan);
			// }
		});
	}
}
