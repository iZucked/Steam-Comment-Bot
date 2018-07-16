/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;

/**
 * Provides groups of slots to apply count restrictions to.
 * @author alex
 *
 */
public interface IMaxSlotConstraintDataProviderEditor extends IMaxSlotCountConstraintDataProvider {
	void addMinLoadSlotsPerMonth(List<ILoadOption> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerMonth(List<ILoadOption> slots, int startMonth, int limit);
	
	void addMinLoadSlotsPerYear(List<ILoadOption> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerYear(List<ILoadOption> slots, int startMonth, int limit);

	void addMinLoadSlotsPerQuarter(List<ILoadOption> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerQuarter(List<ILoadOption> slots, int startMonth, int limit);
	
	void addMinLoadSlotsPerMonthlyPeriod(List<ILoadOption> slots, int startMonth, int period, int limit);
	void addMaxLoadSlotsPerMonthlyPeriod(List<ILoadOption> slots, int startMonth, int period, int limit);

	void addMinDischargeSlotsPerMonth(List<IDischargeOption> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerMonth(List<IDischargeOption> slots, int startMonth, int limit);
	
	void addMinDischargeSlotsPerYear(List<IDischargeOption> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerYear(List<IDischargeOption> slots, int startMonth, int limit);

	void addMinDischargeSlotsPerQuarter(List<IDischargeOption> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerQuarter(List<IDischargeOption> slots, int startMonth, int limit);

	void addMinDischargeSlotsPerMonthlyPeriod(List<IDischargeOption> slots, int startMonth, int period, int limit);
	void addMaxDischargeSlotsPerMonthlyPeriod(List<IDischargeOption> slots, int startMonth, int period, int limit);
}
