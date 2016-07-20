/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.LockedUnusedElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.DifferentSTSVesselsConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PromptRoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.RoundTripVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ShippingHoursRestrictionCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SpotToSpotConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VesselEventConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

/**
 * Utility class for handling scenarios.
 * 
 * @author hinton
 * 
 */
public class ScenarioUtils {

	private static final boolean SPOT_TO_SPOT_CONSTRAINT = true;

	public static Constraint createConstraint(final ParametersFactory parametersFactory, final String name, final boolean enabled) {
		final Constraint c = parametersFactory.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	public static Objective createObjective(final ParametersFactory of, final String name, final double weight) {
		final Objective o = of.createObjective();
		o.setName(name);
		o.setWeight(weight);
		o.setEnabled(weight > 0);
		return o;
	}

	/**
	 * @return
	 */
	@NonNull
	public static OptimiserSettings createDefaultSettings() {
		final ParametersFactory parametersFactory = ParametersFactory.eINSTANCE;

		final OptimiserSettings settings = parametersFactory.createOptimiserSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();

			// "Quick" resource allocation checks
			constraints.add(createConstraint(parametersFactory, ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, RoundTripVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, AllowedVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortExclusionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, VesselEventConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, FOBDESCompatibilityConstraintCheckerFactory.NAME, true));

			constraints.add(createConstraint(parametersFactory, OrderedSequenceElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, TravelTimeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, VirtualVesselConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, TimeSortConstraintCheckerFactory.NAME, true));

			// BugzId: 1597 - Disable as this causes problems with optimisation performance.
			// constraints.add(createConstraint(parametersFactory, SlotGroupCountConstraintCheckerFactory.NAME, true));

			constraints.add(createConstraint(parametersFactory, RestrictedElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ContractCvConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortCvCompatibilityConstraintCheckerFactory.NAME, true));
			if (SPOT_TO_SPOT_CONSTRAINT) {
				constraints.add(createConstraint(parametersFactory, SpotToSpotConstraintCheckerFactory.NAME, true));
			}
			constraints.add(createConstraint(parametersFactory, DifferentSTSVesselsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingTypeRequirementConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingHoursRestrictionCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, LockedUnusedElementsConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives.add(createObjective(parametersFactory, "cargo-scheduler-group-profit", 1));

			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1));
			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 0.1));
			objectives.add(createObjective(parametersFactory, NonOptionalSlotFitnessCoreFactory.NAME, 3_000_000));
			objectives.add(createObjective(parametersFactory, "SimilarityFitnessCore", 1.0));
		}

		final AnnealingSettings annealingSettings = parametersFactory.createAnnealingSettings();
		annealingSettings.setIterations(1_000_000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(10_000);
		annealingSettings.setInitialTemperature(1_000_000);
		// restarts
		annealingSettings.setRestarting(false);
		annealingSettings.setRestartIterationsThreshold(500_000);
		settings.setAnnealingSettings(annealingSettings);

		final OptimisationRange range = parametersFactory.createOptimisationRange();
		settings.setRange(range);
		settings.setSeed(0);

		// similarity
		settings.setSimilaritySettings(createOffSimilaritySettings());

		// hill climbing
		final IndividualSolutionImprovementSettings solutionImprovementSettings = parametersFactory.createIndividualSolutionImprovementSettings();
		solutionImprovementSettings.setImprovingSolutions(true);
		solutionImprovementSettings.setIterations(50_000);

		settings.setSolutionImprovementSettings(solutionImprovementSettings);

		// {
		// {
		// CleanStateOptimisationStage stage = ScenarioUtils.createDefaultCleanStateParameters();
		// stage.getAnnealingSettings().setIterations(10_000);
		// stage.setSeed(0);
		//
		// ParallelOptimisationStage<CleanStateOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
		// pStage.setTemplate(stage);
		// pStage.setJobCount(1);
		//
		// settings.getStages().add(pStage);
		// }
		//
		//
		// {
		// LocalSearchOptimisationStage stage = ParametersFactory.eINSTANCE.createLocalSearchOptimisationStage();
		// stage.setAnnealingSettings(EcoreUtil.copy(settings.getAnnealingSettings()));
		// stage.setSeed(0);
		//
		// ParallelOptimisationStage<LocalSearchOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
		// pStage.setTemplate(stage);
		// pStage.setJobCount(1);
		//
		// settings.getStages().add(pStage);
		// }
		//
		// {
		// HillClimbOptimisationStage stage = ParametersFactory.eINSTANCE.createHillClimbOptimisationStage();
		// stage.setAnnealingSettings(EcoreUtil.copy(settings.getAnnealingSettings()));
		// stage.getAnnealingSettings().setIterations(settings.getSolutionImprovementSettings().getIterations());
		// stage.setSeed(0);
		//
		// ParallelOptimisationStage<HillClimbOptimisationStage> pStage = ParametersFactory.eINSTANCE.createParallelOptimisationStage();
		// pStage.setTemplate(stage);
		// // pStage.setJobCount(headlessParameters.getParameter("clean-state-jobs", Integer.class));
		//
		// settings.getStages().add(pStage);
		// }
		// }
		return settings;
	}

	public static SimilaritySettings createOffSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(8, 0));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 0));
		similaritySettings.setHighInterval(createSimilarityInterval(30, 0));
		similaritySettings.setOutOfBoundsWeight(0);

		return similaritySettings;
	}

	public static SimilaritySettings createLowSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(8, 0));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 0));
		similaritySettings.setHighInterval(createSimilarityInterval(30, 500_000));
		similaritySettings.setOutOfBoundsWeight(5_000_000);

		return similaritySettings;
	}

	public static SimilaritySettings createMediumSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(8, 0));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 250_000));
		similaritySettings.setHighInterval(createSimilarityInterval(30, 500_000));
		similaritySettings.setOutOfBoundsWeight(1_000_000);

		return similaritySettings;
	}

	public static SimilaritySettings createHighSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(8, 250_000));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 500_000));
		similaritySettings.setHighInterval(createSimilarityInterval(30, 1_000_000));
		similaritySettings.setOutOfBoundsWeight(5_000_000);

		return similaritySettings;
	}

	public static SimilarityInterval createSimilarityInterval(final int upperChangeCount, final int weight) {
		final SimilarityInterval interval = ParametersFactory.eINSTANCE.createSimilarityInterval();

		interval.setThreshold(upperChangeCount);
		interval.setWeight(weight);

		return interval;
	}

	@NonNull
	public static UserSettings createDefaultUserSettings() {
		final UserSettings userSettings = ParametersFactory.eINSTANCE.createUserSettings();

		userSettings.setBuildActionSets(false);
		userSettings.setGenerateCharterOuts(false);
		userSettings.setShippingOnly(false);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setFloatingDaysLimit(15);
		return userSettings;
	}

	public static @NonNull CleanStateOptimisationStage createDefaultCleanStateParameters() {
		final CleanStateOptimisationStage params = ParametersFactory.eINSTANCE.createCleanStateOptimisationStage();

		final AnnealingSettings annealingSettings = ParametersFactory.eINSTANCE.createAnnealingSettings();
		annealingSettings.setIterations(50_000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(10_000);
		annealingSettings.setInitialTemperature(1_000_000);
		// restarts
		annealingSettings.setRestarting(false);
		params.setAnnealingSettings(annealingSettings);

		params.setSeed(0);

		return params;
	}

}
