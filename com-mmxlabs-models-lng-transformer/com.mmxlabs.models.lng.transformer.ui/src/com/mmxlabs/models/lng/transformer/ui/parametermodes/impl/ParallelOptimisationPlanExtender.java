/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.ui.parametermodes.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultiobjectiveSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.MultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelHillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelLocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelMultiobjectiveSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelMultipleSolutionSimilarityOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.ScenarioUtils;
import com.mmxlabs.models.lng.transformer.ui.parametermodes.IParameterModeExtender;

public class ParallelOptimisationPlanExtender implements IParameterModeExtender {

	@Override
	public void extend(@NonNull final OptimisationPlan plan) {
		modifyOptimisationPlan(plan);
	}

	/**
	 * Modify the plan in order to swap in Parallel modules
	 * 
	 * @param plan
	 */
	public void modifyOptimisationPlan(@NonNull final OptimisationPlan plan) {
		UserSettings userSettings = plan.getUserSettings();
		EList<OptimisationStage> stages = plan.getStages();
		List<OptimisationStage> newPlan = new LinkedList<>();
		if (userSettings.getSimilarityMode() != SimilarityMode.OFF) {
			for (OptimisationStage stage : plan.getStages()) {
				// remove hill climbing for now
				if (stage instanceof HillClimbOptimisationStage) {
					continue;
				}
				OptimisationStage optimisationStage = EcoreUtil.copy((stage instanceof ParallelOptimisationStage) ? ((ParallelOptimisationStage<?>) stage).getTemplate() : stage);
				if (!(optimisationStage instanceof LocalSearchOptimisationStage || optimisationStage instanceof MultiobjectiveSimilarityOptimisationStage
						|| optimisationStage instanceof MultipleSolutionSimilarityOptimisationStage)) {
					newPlan.add(optimisationStage);
				} else {
					// Modify similarity settings
					if (optimisationStage instanceof LocalSearchOptimisationStage) {
						ConstraintAndFitnessSettings constraintAndFitnessSettings = ((LocalSearchOptimisationStage) optimisationStage).getConstraintAndFitnessSettings();
						SimilaritySettings similaritySettings = constraintAndFitnessSettings.getSimilaritySettings();
						if (similaritySettings != null) {
							constraintAndFitnessSettings.setSimilaritySettings(ScenarioUtils.createUnweightedSimilaritySettings());
						}
					}
					if (optimisationStage instanceof MultiobjectiveSimilarityOptimisationStage) {
						// make it parallel
						ParallelMultiobjectiveSimilarityOptimisationStage parallelMultiobjectiveSimilarityOptimisationStage = ParametersFactory.eINSTANCE
								.createParallelMultiobjectiveSimilarityOptimisationStage();
						parallelMultiobjectiveSimilarityOptimisationStage.setAnnealingSettings(((MultiobjectiveSimilarityOptimisationStage) optimisationStage).getAnnealingSettings());
						parallelMultiobjectiveSimilarityOptimisationStage
								.setConstraintAndFitnessSettings(((MultiobjectiveSimilarityOptimisationStage) optimisationStage).getConstraintAndFitnessSettings());
						parallelMultiobjectiveSimilarityOptimisationStage.setName(((MultiobjectiveSimilarityOptimisationStage) optimisationStage).getName());
						parallelMultiobjectiveSimilarityOptimisationStage.setSeed(((MultiobjectiveSimilarityOptimisationStage) optimisationStage).getSeed());
						newPlan.add(parallelMultiobjectiveSimilarityOptimisationStage);
					} else if (optimisationStage instanceof MultipleSolutionSimilarityOptimisationStage) {
						// make it parallel
						ParallelMultipleSolutionSimilarityOptimisationStage parallelMultipleSolutionSimilarityOptimisationStage = ParametersFactory.eINSTANCE
								.createParallelMultipleSolutionSimilarityOptimisationStage();
						parallelMultipleSolutionSimilarityOptimisationStage.setAnnealingSettings(((MultipleSolutionSimilarityOptimisationStage) optimisationStage).getAnnealingSettings());
						parallelMultipleSolutionSimilarityOptimisationStage
								.setConstraintAndFitnessSettings(((MultipleSolutionSimilarityOptimisationStage) optimisationStage).getConstraintAndFitnessSettings());
						parallelMultipleSolutionSimilarityOptimisationStage.setName(((MultipleSolutionSimilarityOptimisationStage) optimisationStage).getName());
						parallelMultipleSolutionSimilarityOptimisationStage.setSeed(((MultipleSolutionSimilarityOptimisationStage) optimisationStage).getSeed());
						newPlan.add(parallelMultipleSolutionSimilarityOptimisationStage);

					} else if (optimisationStage instanceof LocalSearchOptimisationStage) {
						// make it Multiobjective and parallel
						ParallelMultiobjectiveSimilarityOptimisationStage parallelMultiobjectiveSimilarityOptimisationStage = ParametersFactory.eINSTANCE
								.createParallelMultiobjectiveSimilarityOptimisationStage();
						parallelMultiobjectiveSimilarityOptimisationStage.setAnnealingSettings(((LocalSearchOptimisationStage) optimisationStage).getAnnealingSettings());
						parallelMultiobjectiveSimilarityOptimisationStage.setConstraintAndFitnessSettings(((LocalSearchOptimisationStage) optimisationStage).getConstraintAndFitnessSettings());
						parallelMultiobjectiveSimilarityOptimisationStage.setName(((LocalSearchOptimisationStage) optimisationStage).getName());
						parallelMultiobjectiveSimilarityOptimisationStage.setSeed(((LocalSearchOptimisationStage) optimisationStage).getSeed());
						newPlan.add(parallelMultiobjectiveSimilarityOptimisationStage);
					}
				}
			}
		} else {
			// make things parallel
			for (OptimisationStage stage : plan.getStages()) {
				OptimisationStage optimisationStage = EcoreUtil.copy((stage instanceof ParallelOptimisationStage) ? ((ParallelOptimisationStage<?>) stage).getTemplate() : stage);
				if (optimisationStage instanceof HillClimbOptimisationStage) {
					ParallelHillClimbOptimisationStage parallelHillClimbOptimisationStage = ParametersFactory.eINSTANCE.createParallelHillClimbOptimisationStage();
					parallelHillClimbOptimisationStage.setAnnealingSettings(((HillClimbOptimisationStage) optimisationStage).getAnnealingSettings());
					parallelHillClimbOptimisationStage.setConstraintAndFitnessSettings(((HillClimbOptimisationStage) optimisationStage).getConstraintAndFitnessSettings());
					parallelHillClimbOptimisationStage.setName(((HillClimbOptimisationStage) optimisationStage).getName());
					parallelHillClimbOptimisationStage.setSeed(((HillClimbOptimisationStage) optimisationStage).getSeed());
					newPlan.add(parallelHillClimbOptimisationStage);
				} else if (optimisationStage instanceof LocalSearchOptimisationStage) {
					ParallelLocalSearchOptimisationStage parallelLocalSearchOptimisationStage = ParametersFactory.eINSTANCE.createParallelLocalSearchOptimisationStage();
					parallelLocalSearchOptimisationStage.setAnnealingSettings(((LocalSearchOptimisationStage) optimisationStage).getAnnealingSettings());
					parallelLocalSearchOptimisationStage.setConstraintAndFitnessSettings(((LocalSearchOptimisationStage) optimisationStage).getConstraintAndFitnessSettings());
					parallelLocalSearchOptimisationStage.setName(((LocalSearchOptimisationStage) optimisationStage).getName());
					parallelLocalSearchOptimisationStage.setSeed(((LocalSearchOptimisationStage) optimisationStage).getSeed());
					newPlan.add(parallelLocalSearchOptimisationStage);
				} else {
					newPlan.add(optimisationStage);
				}
			}
		}
		// add modified plan back in
		stages.clear();
		for (OptimisationStage newPlanStage : newPlan) {
			stages.add(newPlanStage);
		}
	}
}