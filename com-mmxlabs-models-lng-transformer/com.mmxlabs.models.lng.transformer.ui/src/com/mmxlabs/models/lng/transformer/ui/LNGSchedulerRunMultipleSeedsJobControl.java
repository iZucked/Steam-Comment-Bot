/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

public class LNGSchedulerRunMultipleSeedsJobControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerRunMultipleSeedsJobControl(final LNGRunMultipleSeedsJobDescriptor jobDescriptor) throws IOException {
		super(jobDescriptor, (originalSettings, factory) -> {
			for (int i = 0; i < 20; ++i) {
				final OptimiserSettings optimiserSettings = EcoreUtil.copy(originalSettings);
				optimiserSettings.setSeed(i);
				String name = "Seed-" + i;
				factory.accept(name, optimiserSettings);
			}
		});
	}
}
