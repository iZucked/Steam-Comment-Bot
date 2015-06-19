/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.lng.transformer.extensions.restrictedelements.RestrictedElementsConstraintCheckerFactory;
import com.mmxlabs.models.lng.transformer.extensions.shippingtype.ShippingTypeRequirementConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.fitness.NonOptionalSlotFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ContractCvConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.DifferentSTSVesselsConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortCvCompatibilityConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.ShippingHoursRestrictionCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SlotGroupCountConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.SpotToSpotConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TimeSortConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.VirtualVesselConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;
import com.mmxlabs.scheduler.optimiser.fitness.SimilarityFitnessCoreFactory;

/**
 * Utility class for handling scenarios.
 * 
 * @author hinton
 * 
 */
public class ScenarioUtils {

	private static final boolean SPOT_TO_SPOT_CONSTRAINT = false;

	public static Constraint createConstraint(ParametersFactory parametersFactory, String name, boolean enabled) {
		Constraint c = parametersFactory.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	public static Objective createObjective(ParametersFactory of, String name, double weight) {
		Objective o = of.createObjective();
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

		OptimiserSettings settings = parametersFactory.createOptimiserSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();
			constraints.add(createConstraint(parametersFactory, ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, OrderedSequenceElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, TravelTimeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortExclusionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, VirtualVesselConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, TimeSortConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, SlotGroupCountConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, RestrictedElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ContractCvConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, PortCvCompatibilityConstraintCheckerFactory.NAME, true));
			if (SPOT_TO_SPOT_CONSTRAINT) {
				constraints.add(createConstraint(parametersFactory, SpotToSpotConstraintCheckerFactory.NAME, true));
			}
			constraints.add(createConstraint(parametersFactory, DifferentSTSVesselsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingTypeRequirementConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingHoursRestrictionCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives.add(createObjective(parametersFactory, "cargo-scheduler-group-profit", 1));

			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1));
			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 0.1));
			objectives.add(createObjective(parametersFactory, NonOptionalSlotFitnessCoreFactory.NAME, 3000000));
			objectives.add(createObjective(parametersFactory, "SimilarityFitnessCore", 0.00001));
		}

		final AnnealingSettings annealingSettings = parametersFactory.createAnnealingSettings();
		annealingSettings.setIterations(1000000);
		annealingSettings.setCooling(0.96);
		annealingSettings.setEpochLength(900); // 900 for full; 300 for period
		annealingSettings.setInitialTemperature(1000000);

		settings.setAnnealingSettings(annealingSettings);

		final OptimisationRange range = parametersFactory.createOptimisationRange();
		settings.setRange(range);
		settings.setSeed(0);
		
		// similarity
		SimilaritySettings similaritySettings = ParametersFactory.eINSTANCE.createSimilaritySettings();
		settings.setSimilaritySettings(similaritySettings);
		return settings;
	}
}
