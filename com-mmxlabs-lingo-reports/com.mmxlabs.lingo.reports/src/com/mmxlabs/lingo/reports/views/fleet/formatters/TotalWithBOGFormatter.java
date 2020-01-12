/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;

public class TotalWithBOGFormatter extends CostFormatter {
	final private boolean withBOG;
	
	public TotalWithBOGFormatter(boolean includeUnits, boolean withBOG) {
		super(includeUnits);
		this.withBOG = withBOG;
	}

	public TotalWithBOGFormatter(boolean includeUnits, boolean withBOG, Type type) {
		super(includeUnits, type);
		this.withBOG = withBOG;
	}
    
	@Override
	public Integer getIntValue(Object object) {
		List<IntegerFormatter> revenueFormatters = new ArrayList<>(2);
		List<IntegerFormatter> costFormatters = new ArrayList<>(7);
		
		costFormatters.add(new BaseFuelCostFormatter(CostFormatter.Type.COST));
		costFormatters.add(new HeelCostFormatter(false, CostFormatter.Type.COST));
		costFormatters.add(new CanalCostFormatter(CostFormatter.Type.COST));
		costFormatters.add(new BallastBonusFormatter(false, CostFormatter.Type.COST));
		costFormatters.add(new CharterCostFormatter(false, CostFormatter.Type.COST));
		costFormatters.add(new PortCostFormatter(CostFormatter.Type.COST));
		costFormatters.add(new RepositioningFeeFormatter(false, CostFormatter.Type.COST));
		costFormatters.add(new CooldownCostFormatter(false));
		if (withBOG) {
			costFormatters.add(new LNGCostFormatter(CostFormatter.Type.COST));
		}

		revenueFormatters.add(new GeneratedCharterRevenueFormatter(true, true, CostFormatter.Type.REVENUE));
		revenueFormatters.add(new HeelRevenueFormatter(false, CostFormatter.Type.REVENUE));

		Integer cost = 0;
		for (IntegerFormatter formatter : costFormatters) {
			Integer res = formatter.getIntValue(object);
			if (res != null) {
				cost += res;
			}
		}

		Integer revenue = 0;
		for (IntegerFormatter formatter : revenueFormatters) {
			Integer res = formatter.getIntValue(object);
			if (res != null) {
				revenue += res;
			}
		}
		
		final int total = cost - revenue;
		return total;
	}
}
