/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.transformer;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.optimiser.AnnealingSettings;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimisationRange;
import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.optimiser.common.constraints.OrderedSequenceElementsConstraintCheckerFactory;
import com.mmxlabs.optimiser.common.constraints.ResourceAllocationConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortExclusionConstraintCheckerFactory;
import com.mmxlabs.scheduler.optimiser.constraints.impl.PortTypeConstraintCheckerFactory;
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
		OptimiserModel om = scenario.getSubModel(OptimiserModel.class);
		OptimiserSettings settings = createDefaultSettings();
		om.getSettings().add(settings);
		om.setActiveSetting(settings);
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
		o.setEnabled(weight > 0);
		return o;
	}

	/**
	 * @return
	 */
	public static OptimiserSettings createDefaultSettings() {
		final OptimiserFactory of = OptimiserPackage.eINSTANCE.getOptimiserFactory();
		
		
		OptimiserSettings settings = of.createOptimiserSettings();

		settings.setName("Default LSO Settings");

		// create constraints
		{
			final EList<Constraint> constraints = settings.getConstraints();
			constraints.add(createConstraint(of, ResourceAllocationConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, OrderedSequenceElementsConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, PortTypeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, TravelTimeConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, PortExclusionConstraintCheckerFactory.NAME, true));
			constraints.add(createConstraint(of, VirtualVesselConstraintCheckerFactory.NAME, true));
		}

		// create objectives
		{
			final EList<Objective> objectives = settings.getObjectives();
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_COST_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_BASE_COMPONENT_NAME, 1));

			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_COOLDOWN_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.COST_LNG_COMPONENT_NAME, 1));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.ROUTE_PRICE_COMPONENT_NAME, 1));

			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CHARTER_REVENUE_COMPONENT_NAME, 1));

//			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.PROFIT_COMPONENT_NAME, 1));
			
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.LATENESS_COMPONENT_NAME, 100000));
			objectives.add(createObjective(of, CargoSchedulerFitnessCoreFactory.CAPACITY_COMPONENT_NAME, 100000));
		}

		final AnnealingSettings annealingSettings = of.createAnnealingSettings();
//		annealingSettings.setIterations(200000);
		annealingSettings.setIterations(1000);
		annealingSettings.setCooling(0.85);
		annealingSettings.setEpochLength(20000);
		annealingSettings.setInitialTemperature(45000);
		
		settings.setAnnealingSettings(annealingSettings);
		
		final OptimisationRange range = of.createOptimisationRange();
		settings.setRange(range);
		return settings;
	}
}
