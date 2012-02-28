/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import org.eclipse.emf.common.util.EList;

import scenario.Scenario;
import scenario.optimiser.Constraint;
import scenario.optimiser.Objective;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimiserFactory;
import scenario.optimiser.OptimiserPackage;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoFactory;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.ThresholderSettings;

import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.TravelTimeConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCoreFactory;

/**
 * Utility class for handling scenarios.
 * 
 * @author hinton
 * 
 */
public class ScenarioUtils {
	/**
	 * Add some standard optimiser settings to the given scenario
	 * 
	 * Copied from RandomScenarioUtils, due to build cycle issues.
	 * 
	 * TODO check these are reasonable settings (num. iters etc)
	 * 
	 * @param scenario
	 */
	public static void addDefaultSettings(MMXRootObject scenario) {
		final OptimiserFactory of = OptimiserPackage.eINSTANCE.getOptimiserFactory();
		final LsoFactory lsof = LsoPackage.eINSTANCE.getLsoFactory();

		Optimisation optimisation = of.createOptimisation();

		LSOSettings settings = lsof.createLSOSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();
			constraints.add(createConstraint(of, ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, OrderedSequenceElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, TravelTimeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, PortExclusionConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, 1));

			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, 1));

			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, 1));

			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.PROFIT_COMPONENT_NAME, 1));
		}

		settings.setNumberOfSteps(200000);
		ThresholderSettings thresholderSettings = lsof.createThresholderSettings();
		thresholderSettings.setAlpha(0.85);
		thresholderSettings.setEpochLength(20000);
		thresholderSettings.setInitialAcceptanceRate(45000);
		settings.setThresholderSettings(thresholderSettings);

		optimisation.getAllSettings().add(settings);
		optimisation.setCurrentSettings(settings);
		scenario.setOptimisation(optimisation);

		if (scenario.getScheduleModel().getSchedules().isEmpty() == false) {
			settings.setInitialSchedule(scenario.getScheduleModel().getSchedules().get(0));
		}
	}

	private static Constraint createConstraint(OptimiserFactory of, String name, boolean enabled) {
		Constraint c = of.createConstraint();
		c.setName(name);
		c.setEnabled(enabled);
		return c;
	}

	private static Objective createObjective(OptimiserFactory of, String name, double weight) {
		Objective o = of.createObjective();
		o.setName(name);
		o.setWeight(weight);
		return o;
	}
}
