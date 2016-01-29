/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness;

/**
 * An interface for an OSGi service which provides additional cargo fitness components from other bundles
 * 
 * @author hinton
 *
 */
public interface ICargoFitnessComponentProvider {
	/**
	 * The name of the fitness component being created
	 * @return
	 */
	public String getFitnessComponentName();
	/**
	 * Method to create the component
	 * @param core
	 * @return
	 */
	public ICargoFitnessComponent createComponent(final CargoSchedulerFitnessCore core);
}
