/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer.extensions;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
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

/**
 * Utility class for handling scenarios.
 * 
 * @author hinton
 * 
 */
public class ScenarioUtils {

	private static Constraint createConstraint(ParametersFactory parametersFactory, String name, boolean enabled) {
		Constraint c = parametersFactory.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	private static Objective createObjective(ParametersFactory of, String name, double weight) {
		Objective o = of.createObjective();
		o.setName(name);
		o.setWeight(weight);
		o.setEnabled(weight > 0);
		return o;
	}

	/**
	 * @return
	 */
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
			constraints.add(createConstraint(parametersFactory, SpotToSpotConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, DifferentSTSVesselsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingTypeRequirementConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(parametersFactory, ShippingHoursRestrictionCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, 1));
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, 1));
			//
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, 1));
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, 1));
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, 1));
			//
			// objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, 1));

			objectives.add(createObjective(parametersFactory, "cargo-scheduler-group-profit", 1));

			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1000000));
			objectives.add(createObjective(parametersFactory, CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 5000000));
			objectives.add(createObjective(parametersFactory, NonOptionalSlotFitnessCoreFactory.NAME, 3000000));
		}

		final AnnealingSettings annealingSettings = parametersFactory.createAnnealingSettings();
		// annealingSettings.setIterations(200000);
		annealingSettings.setIterations(80000);
		annealingSettings.setCooling(0.85);
		annealingSettings.setEpochLength(10000);
		annealingSettings.setInitialTemperature(45000);

		settings.setAnnealingSettings(annealingSettings);

		final OptimisationRange range = parametersFactory.createOptimisationRange();
		settings.setRange(range);
		return settings;
	}
}
