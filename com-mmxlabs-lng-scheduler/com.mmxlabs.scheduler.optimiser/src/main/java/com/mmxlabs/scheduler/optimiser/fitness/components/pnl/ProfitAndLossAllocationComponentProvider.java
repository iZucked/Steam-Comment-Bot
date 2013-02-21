/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.fitness.components.pnl;

import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponentProvider;

public class ProfitAndLossAllocationComponentProvider implements ICargoFitnessComponentProvider {

	public static final String PROFIT_COMPONENT_NAME = "cargo-scheduler-group-profit";

	@Override
	public String getFitnessComponentName() {
		return PROFIT_COMPONENT_NAME;
	}

	@Override
	public ICargoFitnessComponent createComponent(CargoSchedulerFitnessCore core) {
		return new ProfitAndLossAllocationComponent(PROFIT_COMPONENT_NAME, core);
	}
}
