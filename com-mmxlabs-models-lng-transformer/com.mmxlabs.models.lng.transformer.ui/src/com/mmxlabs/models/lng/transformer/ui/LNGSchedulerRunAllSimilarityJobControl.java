/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;

public class LNGSchedulerRunAllSimilarityJobControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerRunAllSimilarityJobControl(final LNGRunAllSimilarityJobDescriptor jobDescriptor) throws IOException {
		super(jobDescriptor, (originalSettings, factory) -> {
			for (int i = 0; i < 4; ++i) {
				final OptimiserSettings optimiserSettings = EcoreUtil.copy(originalSettings);
				String name;
				switch (i) {
				case 0:
					optimiserSettings.setSimilaritySettings(ScenarioUtils.createOffSimilaritySettings());
					optimiserSettings.setBuildActionSets(false);
					optimiserSettings.getAnnealingSettings().setRestarting(false);
					name = "Sim. Off";
					break;
				case 1:
					optimiserSettings.setSimilaritySettings(ScenarioUtils.createLowSimilaritySettings());
					optimiserSettings.getAnnealingSettings().setRestarting(false);
					name = "Sim. Low";
					break;
				case 2:
					optimiserSettings.setSimilaritySettings(ScenarioUtils.createMediumSimilaritySettings());
					optimiserSettings.getAnnealingSettings().setRestarting(false);
					name = "Sim. Medium";
					break;
				case 3:
					optimiserSettings.setSimilaritySettings(ScenarioUtils.createHighSimilaritySettings());
					name = "Sim. High";
					break;
				default:
					throw new IllegalStateException();
				}
				factory.accept(name, optimiserSettings);
			}
		});
	}

}
