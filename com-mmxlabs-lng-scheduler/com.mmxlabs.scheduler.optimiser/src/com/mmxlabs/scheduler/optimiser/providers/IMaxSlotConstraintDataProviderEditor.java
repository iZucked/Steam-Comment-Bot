/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.scheduler.optimiser.providers;

import java.util.List;

import com.mmxlabs.common.Pair;
import com.mmxlabs.scheduler.optimiser.components.IDischargeOption;
import com.mmxlabs.scheduler.optimiser.components.ILoadOption;
import com.mmxlabs.scheduler.optimiser.components.util.MonthlyDistributionConstraint;

/**
 * Provides groups of slots to apply count restrictions to.
 * @author alex
 *
 */
public interface IMaxSlotConstraintDataProviderEditor<P,C> extends IMaxSlotCountConstraintDataProvider<P,C> {
	void addMinLoadSlotsPerMonth(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerMonth(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);
	
	void addMinLoadSlotsPerYear(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerYear(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);

	void addMinLoadSlotsPerQuarter(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);
	void addMaxLoadSlotsPerQuarter(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int limit);
	
	void addMinLoadSlotsPerMonthlyPeriod(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int period, int limit);
	void addMaxLoadSlotsPerMonthlyPeriod(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, int startMonth, int period, int limit);

	void addMinDischargeSlotsPerMonth(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerMonth(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);
	
	void addMinDischargeSlotsPerYear(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerYear(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);

	void addMinDischargeSlotsPerQuarter(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);
	void addMaxDischargeSlotsPerQuarter(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int limit);

	void addMinDischargeSlotsPerMonthlyPeriod(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int period, int limit);
	void addMaxDischargeSlotsPerMonthlyPeriod(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, int startMonth, int period, int limit);
	
	void addMinMaxLoadSlotsPerMultiMonthPeriod(P contractProfile, C profileConstraint, List<Pair<ILoadOption,Integer>> slots, MonthlyDistributionConstraint monthlyDistributionConstraint);
	void addMinMaxDischargeSlotsPerMultiMonthPeriod(P contractProfile, C profileConstraint, List<Pair<IDischargeOption,Integer>> slots, MonthlyDistributionConstraint monthlyDistributionConstraint);
}
