package com.mmxlabs.trading.optimiser.components;

import com.mmxlabs.scheduler.optimiser.SchedulerConstants;
import com.mmxlabs.scheduler.optimiser.fitness.CargoSchedulerFitnessCore;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponent;
import com.mmxlabs.scheduler.optimiser.fitness.ICargoFitnessComponentProvider;
import com.mmxlabs.trading.optimiser.TradingConstants;

public class ProfitAndLossAllocationComponentProvider implements ICargoFitnessComponentProvider {

	public static final String PROFIT_COMPONENT_NAME = "cargo-scheduler-group-profit";

	@Override
	public String getFitnessComponentName() {
		return PROFIT_COMPONENT_NAME;
	}

	@Override
	public ICargoFitnessComponent createComponent(CargoSchedulerFitnessCore core) {
		return new ProfitAndLossAllocationComponent(PROFIT_COMPONENT_NAME, TradingConstants.DCP_entityProvider, SchedulerConstants.DCP_vesselProvider, SchedulerConstants.DCP_portSlotsProvider, core);
	}
}
