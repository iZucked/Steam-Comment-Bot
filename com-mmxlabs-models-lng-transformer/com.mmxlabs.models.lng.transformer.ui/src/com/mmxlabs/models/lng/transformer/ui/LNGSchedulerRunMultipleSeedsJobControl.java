/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

public class LNGSchedulerRunMultipleSeedsJobControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerRunMultipleSeedsJobControl(final LNGRunMultipleSeedsJobDescriptor jobDescriptor) throws IOException {
		super(jobDescriptor, (originalPlan, factory) -> {
//			for (int i = 0; i < 20; ++i) {
//				final OptimisationPlan optimisationPlan = EcoreUtil.copy(originalPlan);
//				String name = "Seed-" + i;
//				optimisationPlan.setSeed(i);
//				factory.accept(name, optimisationPlan);
//			}
		});
	}
}
