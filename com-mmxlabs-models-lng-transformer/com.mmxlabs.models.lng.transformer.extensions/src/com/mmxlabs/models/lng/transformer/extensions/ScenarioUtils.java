/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions;

import java.time.YearMonth;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.time.Months;
import com.mmxlabs.models.lng.parameters.ActionPlanOptimisationStage;
import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.LocalSearchOptimisationStage;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.OptimisationStage;
import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilarityMode;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.parameters.SolutionBuilderSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.transformer.extensions.panamaslots.PanamaSlotsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.LockedUnusedElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.AllowedVesselPermissionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.DifferentSTSVesselsConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.FOBDESCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.LadenLegLimitConstraintCheckerFactory;
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
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.components.NonOptionalSlotFitnessCoreFactory;

/**
 * Utility class for handling scenarios.
 * 
 * @author hinton
 * 
 */
public class ScenarioUtils {

	private static final boolean SPOT_TO_SPOT_CONSTRAINT = true;

	public static Constraint createConstraint(final String name, final boolean enabled) {
		final Constraint c = ParametersFactory.eINSTANCE.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	public static Objective createObjective(final String name, final double weight) {
		final Objective o = ParametersFactory.eINSTANCE.createObjective();
		o.setName(name);
		o.setWeight(weight);
		o.setEnabled(weight > 0);
		return o;
	}

	public static Objective createObjective(final String name, final double weight, boolean enabled) {
		final Objective o = ParametersFactory.eINSTANCE.createObjective();
		o.setName(name);
		o.setWeight(weight);
		o.setEnabled(enabled);
		return o;
	}

	@NonNull
	public static OptimisationPlan createDefaultOptimisationPlan() {

		final OptimisationPlan plan = ParametersFactory.eINSTANCE.createOptimisationPlan();

		plan.setUserSettings(createDefaultUserSettings());

		plan.setSolutionBuilderSettings(createDefaultSolutionBuilderSettings());

		@NonNull
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = createDefaultConstraintAndFitnessSettings();

		// plan.getStages().add(createDefaultCleanStateParameters(EcoreUtil.copy(constraintAndFitnessSettings)));
		plan.getStages().add(createDefaultLSOParameters(EcoreUtil.copy(constraintAndFitnessSettings)));
		plan.getStages().add(createDefaultHillClimbingParameters(EcoreUtil.copy(constraintAndFitnessSettings)));

		return plan;
	}

	public static SimilaritySettings createOffSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
		int weight = 0;
		// DEMO: Demo tweak
		if (System.getProperty("similarity.smallweight") != null) {
			weight = 10_000;
		}
		similaritySettings.setLowInterval(createSimilarityInterval(8, weight));
		similaritySettings.setMedInterval(createSimilarityInterval(16, weight));
		similaritySettings.setHighInterval(createSimilarityInterval(32, weight));
		similaritySettings.setOutOfBoundsWeight(weight);

		return similaritySettings;
	}

	public static SimilaritySettings createUnweightedSimilaritySettings() {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(8, 1));
		similaritySettings.setMedInterval(createSimilarityInterval(16, 1));
		similaritySettings.setHighInterval(createSimilarityInterval(30, 1));
		similaritySettings.setOutOfBoundsWeight(1);

		return similaritySettings;
	}

	public static SimilaritySettings createSimilaritySettings(int lowInterval, int lowWeight, int medInterval, int medWeight, int highInterval, int highWeight, int outOfBounds) {
		final SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();

		similaritySettings.setLowInterval(createSimilarityInterval(lowInterval, lowWeight));
		similaritySettings.setMedInterval(createSimilarityInterval(medInterval, medWeight));
		similaritySettings.setHighInterval(createSimilarityInterval(highInterval, highWeight));
		similaritySettings.setOutOfBoundsWeight(outOfBounds);

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
		userSettings.setWithSpotCargoMarkets(true);
		userSettings.setSimilarityMode(SimilarityMode.OFF);
		userSettings.setFloatingDaysLimit(15);
		return userSettings;
	}

	public static @NonNull LocalSearchOptimisationStage createDefaultLSOParameters(@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final LocalSearchOptimisationStage params = ParametersFactory.eINSTANCE.createLocalSearchOptimisationStage();
		params.setName("lso");
		final AnnealingSettings annealingSettings = ParametersFactory.eINSTANCE.createAnnealingSettings();
		annealingSettings.setIterations(1_000_000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(10_000);
		annealingSettings.setInitialTemperature(1_000_000);
		// restarts
		annealingSettings.setRestarting(false);
		annealingSettings.setRestartIterationsThreshold(500_000);
		params.setAnnealingSettings(annealingSettings);

		params.setSeed(0);

		params.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

		return params;
	}

	public static @NonNull HillClimbOptimisationStage createDefaultHillClimbingParameters(@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final HillClimbOptimisationStage params = ParametersFactory.eINSTANCE.createHillClimbOptimisationStage();
		params.setName("hill");

		final AnnealingSettings annealingSettings = ParametersFactory.eINSTANCE.createAnnealingSettings();
		annealingSettings.setIterations(50_000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(10_000);
		annealingSettings.setInitialTemperature(1_000_000);
		// restarts
		annealingSettings.setRestarting(false);
		annealingSettings.setRestartIterationsThreshold(500_000);
		params.setAnnealingSettings(annealingSettings);

		params.setSeed(0);

		params.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

		return params;
	}

	public static @NonNull ActionPlanOptimisationStage createDefaultActionPlanParameters(@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final ActionPlanOptimisationStage params = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
		params.setName("actionset");

		params.setTotalEvaluations(5_000_000);
		params.setInRunEvaluations(1_500_000);
		params.setSearchDepth(5_000);

		params.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

		return params;
	}

	public static ActionPlanOptimisationStage getActionPlanSettings(@NonNull final SimilarityMode similarityMode, @NonNull final YearMonth start, @NonNull final YearMonth end,
			@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final int monthsInPeriod = Months.between(start, end);
		final ActionPlanOptimisationStage actionPlanSettings;

		switch (similarityMode) {
		case HIGH:
			actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
			actionPlanSettings.setTotalEvaluations(roundToInt(3_000_000 / 3.0 * monthsInPeriod));
			actionPlanSettings.setInRunEvaluations(roundToInt(1_000_000 / 3.0 * monthsInPeriod));
			actionPlanSettings.setSearchDepth(5_000);
			break;
		case MEDIUM:
			actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
			if (monthsInPeriod < 4) {
				actionPlanSettings.setTotalEvaluations(roundToInt(5_000_000 / 3.0 * monthsInPeriod));
				actionPlanSettings.setInRunEvaluations(roundToInt(Math.min(2_000_000, 1_500_000 / 3.0 * monthsInPeriod)));
				actionPlanSettings.setSearchDepth(5_000);
			} else {
				actionPlanSettings.setTotalEvaluations(roundToInt(30_000_000 / 6.0 * monthsInPeriod));
				actionPlanSettings.setInRunEvaluations(2_000_000);
				actionPlanSettings.setSearchDepth(5_000);
			}
			break;
		case LOW:
			actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
			actionPlanSettings.setTotalEvaluations(roundToInt(30_000_000 / 3.0 * monthsInPeriod));
			actionPlanSettings.setInRunEvaluations(roundToInt(Math.min(2_000_000, 2_000_000 / 3.0 * monthsInPeriod)));
			actionPlanSettings.setSearchDepth(5_000);
			break;
		default:
			actionPlanSettings = ParametersFactory.eINSTANCE.createActionPlanOptimisationStage();
			actionPlanSettings.setTotalEvaluations(5_000_000);
			actionPlanSettings.setInRunEvaluations(1_500_000);
			actionPlanSettings.setSearchDepth(5_000);
			break;
		}
		actionPlanSettings.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

		return actionPlanSettings;
	}

	public static @NonNull CleanStateOptimisationStage createDefaultCleanStateParameters(@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final CleanStateOptimisationStage params = ParametersFactory.eINSTANCE.createCleanStateOptimisationStage();
		params.setName("cleanstate");
		final AnnealingSettings annealingSettings = ParametersFactory.eINSTANCE.createAnnealingSettings();
		annealingSettings.setIterations(50_000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(10_000);
		annealingSettings.setInitialTemperature(1_000_000);
		// restarts
		annealingSettings.setRestarting(false);
		params.setAnnealingSettings(annealingSettings);

		params.setSeed(0);

		params.setConstraintAndFitnessSettings(constraintAndFitnessSettings);

		return params;
	}

	@NonNull
	public static SolutionBuilderSettings createDefaultSolutionBuilderSettings() {
		@NonNull
		final ConstraintAndFitnessSettings constraintAndFitnessSettings = createDefaultConstraintAndFitnessSettings();
		// Fitness not required.
		// TODO: Keep fitnesses and evaluate to get initial fitness value rather than having to call evaluate to do so.
		// constraintAndFitnessSettings.getObjectives().clear();
		{
			final Iterator<Objective> itr = constraintAndFitnessSettings.getObjectives().iterator();
			while (itr.hasNext()) {
				final Objective c = itr.next();
				if (SimilarityFitnessCoreFactory.NAME.equals(c.getName())) {
					itr.remove();
				}
			}
		}

		// Filter out constraints not required.
		{
			final Iterator<Constraint> itr = constraintAndFitnessSettings.getConstraints().iterator();
			while (itr.hasNext()) {
				final Constraint c = itr.next();
				if (LockedUnusedElementsConstraintCheckerFactory.NAME.equals(c.getName())) {
					itr.remove();
				}
			}
		}
		return createDefaultSolutionBuilderSettings(constraintAndFitnessSettings);
	}

	@NonNull
	public static SolutionBuilderSettings createDefaultSolutionBuilderSettings(@NonNull final ConstraintAndFitnessSettings constraintAndFitnessSettings) {
		final SolutionBuilderSettings solutionBuilderSettings = ParametersFactory.eINSTANCE.createSolutionBuilderSettings();
		solutionBuilderSettings.setConstraintAndFitnessSettings(constraintAndFitnessSettings);
		return solutionBuilderSettings;
	}

	@NonNull
	public static ConstraintAndFitnessSettings createDefaultConstraintAndFitnessSettings() {

		final ConstraintAndFitnessSettings settings = ParametersFactory.eINSTANCE.createConstraintAndFitnessSettings();

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();

			// "Quick" resource allocation checks
			constraints.add(createConstraint(ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(RoundTripVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(PromptRoundTripVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(AllowedVesselPermissionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(PortExclusionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(VesselEventConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(FOBDESCompatibilityConstraintCheckerFactory.NAME, true));

			constraints.add(createConstraint(OrderedSequenceElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(TravelTimeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(VirtualVesselConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(TimeSortConstraintCheckerFactory.NAME, true));

			// BugzId: 1597 - Disable as this causes problems with optimisation performance.
			// constraints.add(createConstraint(parametersFactory, SlotGroupCountConstraintCheckerFactory.NAME, true));

			constraints.add(createConstraint(RestrictedElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(ContractCvConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(PortCvCompatibilityConstraintCheckerFactory.NAME, true));
			if (SPOT_TO_SPOT_CONSTRAINT) {
				constraints.add(createConstraint(SpotToSpotConstraintCheckerFactory.NAME, true));
			}
			constraints.add(createConstraint(DifferentSTSVesselsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(ShippingTypeRequirementConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(ShippingHoursRestrictionCheckerFactory.NAME, true));
			constraints.add(createConstraint(LockedUnusedElementsConstraintCheckerFactory.NAME, true));
//			constraints.add(createConstraint(PanamaSlotsConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives.add(createObjective("cargo-scheduler-group-profit", 1));

			objectives.add(createObjective(CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1));
			objectives.add(createObjective(CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 0.1));
			objectives.add(createObjective(NonOptionalSlotFitnessCoreFactory.NAME, 3_000_000));
			objectives.add(createObjective("SimilarityFitnessCore", 1.0));
		}

		// similarity
		settings.setSimilaritySettings(createOffSimilaritySettings());

		return settings;
	}

	private static int roundToInt(final double d) {
		return (int) Math.ceil(d);
	}

	public static void setLSOStageIterations(final OptimisationPlan plan, final int iterations) {
		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof LocalSearchOptimisationStage) {
				final LocalSearchOptimisationStage localSearchOptimisationStage = (LocalSearchOptimisationStage) stage;
				localSearchOptimisationStage.getAnnealingSettings().setIterations(iterations);
			}
		}
	}

	public static void setLSOStageSeed(final OptimisationPlan plan, final int seed) {
		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof LocalSearchOptimisationStage) {
				final LocalSearchOptimisationStage localSearchOptimisationStage = (LocalSearchOptimisationStage) stage;
				localSearchOptimisationStage.setSeed(seed);
			}
		}
	}

	public static void setCleanStateStageIterations(final OptimisationPlan plan, final int iterations) {
		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof CleanStateOptimisationStage) {
				final CleanStateOptimisationStage cleanStateOptimisationStage = (CleanStateOptimisationStage) stage;
				cleanStateOptimisationStage.getAnnealingSettings().setIterations(iterations);
			}
		}
	}

	public static void setHillClimbStageIterations(final OptimisationPlan plan, final int iterations) {
		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof HillClimbOptimisationStage) {
				final HillClimbOptimisationStage hillClimbOptimisationStage = (HillClimbOptimisationStage) stage;
				hillClimbOptimisationStage.getAnnealingSettings().setIterations(iterations);
			}
		}
	}

	public static void setActionPlanStageParameters(final OptimisationPlan plan, final int totalEvalations, final int inRunEvaluations, final int searchDepth) {
		for (final OptimisationStage stage : plan.getStages()) {
			if (stage instanceof ActionPlanOptimisationStage) {
				final ActionPlanOptimisationStage actionPlanOptimisationStage = (ActionPlanOptimisationStage) stage;
				actionPlanOptimisationStage.setTotalEvaluations(totalEvalations);
				actionPlanOptimisationStage.setInRunEvaluations(inRunEvaluations);
				actionPlanOptimisationStage.setSearchDepth(searchDepth);
			}
		}
	}

	public static void createOrUpdateAllConstraints(OptimisationPlan plan, String name, boolean enabled) {
		for (OptimisationStage stage : plan.getStages()) {
			if (stage instanceof ParallelOptimisationStage) {
				ParallelOptimisationStage parallelOptimisationStage = (ParallelOptimisationStage) stage;
				stage = parallelOptimisationStage.getTemplate();
			}
			if (stage instanceof ConstraintsAndFitnessSettingsStage) {
				ConstraintAndFitnessSettings settings = ((ConstraintsAndFitnessSettingsStage) stage).getConstraintAndFitnessSettings();
				createOrUpdateContraints(name, enabled, settings);
			}
		}
		SolutionBuilderSettings solutionBuilderSettings = plan.getSolutionBuilderSettings();
		if (solutionBuilderSettings != null) {
			ConstraintAndFitnessSettings settings = solutionBuilderSettings.getConstraintAndFitnessSettings();
			createOrUpdateContraints(name, enabled, settings);
		}
	}

	public static void createOrUpdateContraints(String name, boolean enabled, ConstraintAndFitnessSettings settings) {
		List<Constraint> constraints = settings.getConstraints();
		for (Constraint constraint : constraints) {
			if (constraint.getName().equals(name)) {
				constraint.setEnabled(enabled);
				return;
			}
		}
		constraints.add(createConstraint(name, enabled));
	}

	public static boolean isConstraintInSettings(List<Constraint> constraints, String constraintName) {
		for (Constraint constraint : constraints) {
			if (constraint.getName().equals(constraintName)) {
				return true;
			}
		}
		return false;
	}

	public static void createOrUpdateAllObjectives(OptimisationPlan plan, String name, boolean enabled, double weight) {
		for (OptimisationStage stage : plan.getStages()) {
			if (stage instanceof ParallelOptimisationStage) {
				ParallelOptimisationStage parallelOptimisationStage = (ParallelOptimisationStage) stage;
				stage = parallelOptimisationStage.getTemplate();
			}
			if (stage instanceof ConstraintsAndFitnessSettingsStage) {
				ConstraintAndFitnessSettings settings = ((ConstraintsAndFitnessSettingsStage) stage).getConstraintAndFitnessSettings();
				createOrUpdateObjective(name, enabled, weight, settings);
			}
		}
		SolutionBuilderSettings solutionBuilderSettings = plan.getSolutionBuilderSettings();
		if (solutionBuilderSettings != null) {
			ConstraintAndFitnessSettings settings = solutionBuilderSettings.getConstraintAndFitnessSettings();
			// No fitness (yet)
			createOrUpdateObjective(name, enabled, weight, settings);
		}
	}

	public static void createOrUpdateObjective(String name, boolean enabled, double weight, ConstraintAndFitnessSettings settings) {
		List<Objective> objectives = settings.getObjectives();
		for (Objective objective : objectives) {
			if (objective.getName().equals(name)) {
				objective.setEnabled(true);
				objective.setWeight(weight);
				return;
			}
		}
		objectives.add(createObjective(name, weight, enabled));
	}

}
