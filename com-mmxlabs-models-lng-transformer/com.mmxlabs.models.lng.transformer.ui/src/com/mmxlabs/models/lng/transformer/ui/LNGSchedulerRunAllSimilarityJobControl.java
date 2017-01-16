/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui;

import java.io.IOException;

public class LNGSchedulerRunAllSimilarityJobControl extends AbstractLNGRunMultipleForkedJobsControl {

	public LNGSchedulerRunAllSimilarityJobControl(final LNGRunAllSimilarityJobDescriptor jobDescriptor) throws IOException {
		super(jobDescriptor, (originalPlan, factory) -> {
//			for (int i = 0; i < 4; ++i) {
//				final OptimisationPlan optimisationPlan = EcoreUtil.copy(originalPlan);
//				ConstraintAndFitnessSettings constraintAndFitnessSettings = optimisationPlan.getConstraintAndFitnessSettings();
//				final UserSettings optimiserSettings = optimisationPlan.getUserSettings();
//				String name;
//				switch (i) {
//				case 0:
//					constraintAndFitnessSettings.setSimilaritySettings(ScenarioUtils.createOffSimilaritySettings());
//					optimiserSettings.setBuildActionSets(false);
//					optimiserSettings.getAnnealingSettings().setRestarting(false);
//					name = "Sim. Off";
//					break;
//				case 1:
//					constraintAndFitnessSettings.setSimilaritySettings(ScenarioUtils.createLowSimilaritySettings());
//					optimiserSettings.getAnnealingSettings().setRestarting(false);
//					name = "Sim. Low";
//					break;
//				case 2:
//					constraintAndFitnessSettings.setSimilaritySettings(ScenarioUtils.createMediumSimilaritySettings());
//					optimiserSettings.getAnnealingSettings().setRestarting(false);
//					name = "Sim. Medium";
//					break;
//				case 3:
//					constraintAndFitnessSettings.setSimilaritySettings(ScenarioUtils.createHighSimilaritySettings());
//					name = "Sim. High";
//					break;
//				default:
//					throw new IllegalStateException();
//				}
//				factory.accept(name, optimisationPlan);
//			}
		});
	}

}
