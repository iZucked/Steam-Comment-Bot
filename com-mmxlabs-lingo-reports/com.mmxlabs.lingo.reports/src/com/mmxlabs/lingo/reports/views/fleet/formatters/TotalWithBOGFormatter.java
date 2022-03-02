/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.ArrayList;
import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.formatters.ICostTypeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;

public class TotalWithBOGFormatter extends CostFormatter {
	private final boolean withBOG;

	public TotalWithBOGFormatter(final boolean includeUnits, final boolean withBOG) {
		super(includeUnits);
		this.withBOG = withBOG;
	}

	public TotalWithBOGFormatter(final boolean includeUnits, final boolean withBOG, final Type type) {
		super(includeUnits, type);
		this.withBOG = withBOG;
	}

	@Override
	public Integer getIntValue(final Object object) {
		final List<IntegerFormatter> revenueFormatters = new ArrayList<>(2);
		final List<IntegerFormatter> costFormatters = new ArrayList<>(9);

		costFormatters.add(new BaseFuelCostFormatter(ICostTypeFormatter.Type.COST));
		costFormatters.add(new HeelCostFormatter(false, ICostTypeFormatter.Type.COST));
		costFormatters.add(new CanalCostFormatter(ICostTypeFormatter.Type.COST));
		costFormatters.add(new BallastBonusFormatter(false, ICostTypeFormatter.Type.COST));
		costFormatters.add(new CharterCostFormatter(false, ICostTypeFormatter.Type.COST));
		costFormatters.add(new PortCostFormatter(ICostTypeFormatter.Type.COST));
		costFormatters.add(new RepositioningFeeFormatter(false, ICostTypeFormatter.Type.COST));
		costFormatters.add(new CooldownCostFormatter(false));
		if (withBOG) {
			costFormatters.add(new LNGCostFormatter(ICostTypeFormatter.Type.COST));
		}

		revenueFormatters.add(new GeneratedCharterRevenueFormatter(ICostTypeFormatter.Type.REVENUE));
		revenueFormatters.add(new HeelRevenueFormatter(false, ICostTypeFormatter.Type.REVENUE));

		Integer cost = 0;
		for (final IntegerFormatter formatter : costFormatters) {
			final Integer res = formatter.getIntValue(object);
			if (res != null) {
				cost += res;
			}
		}

		Integer revenue = 0;
		for (final IntegerFormatter formatter : revenueFormatters) {
			final Integer res = formatter.getIntValue(object);
			if (res != null) {
				revenue += res;
			}
		}

		final int total = cost - revenue;
		return total;
	}
}
