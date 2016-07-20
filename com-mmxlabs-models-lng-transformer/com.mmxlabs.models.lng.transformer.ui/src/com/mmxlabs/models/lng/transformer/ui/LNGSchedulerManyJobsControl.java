/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;

public class LNGSchedulerManyJobsControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerManyJobsControl(final LNGRunAllSimilarityJobDescriptor jobDescriptor) throws IOException {

		super(jobDescriptor, (originalSettings, factory) -> {
			int numCopies = 1;
			for (int i = 0; i < numCopies; ++i) {
				final OptimiserSettings optimiserSettings = EcoreUtil.copy(jobDescriptor.getOptimiserSettings());
				String name = String.format("Job %02d", i);
				// optimiserSettings.setSeed(i);
				factory.accept(name, optimiserSettings);
			}
		});
	}
}
