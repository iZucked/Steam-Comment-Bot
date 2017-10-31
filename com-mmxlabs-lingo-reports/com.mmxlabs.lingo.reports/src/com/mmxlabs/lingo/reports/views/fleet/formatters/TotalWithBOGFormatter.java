/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils.ShippingCostType;

public class TotalWithBOGFormatter extends CostFormatter {
	final private boolean withBOG;
	
	public TotalWithBOGFormatter(boolean includeUnits, boolean withBOG) {
		super(includeUnits);
		this.withBOG = withBOG;
	}

	@Override
	public Integer getIntValue(Object object) {
		BaseFuelCostFormatter baseFuelCostFormatter = new BaseFuelCostFormatter();
		HeelCostFormatter heelCostFormatter = new HeelCostFormatter(false);
		BallastBonusFormatter ballastBonusFormatter = new BallastBonusFormatter(false);
		CharterCostFormatter charterCostFormatter = new CharterCostFormatter(false);
		LNGCostFormatter lngCostFormatter = new LNGCostFormatter();
		PortCostFormatter portCostFormatter = new PortCostFormatter();
		RepositioningFeeFormatter repositioningFeeFormatter = new RepositioningFeeFormatter(false);
		
		GeneratedCharterRevenueFormatter generatedCharterRevenueFormatter = new GeneratedCharterRevenueFormatter(true, true);
		HeelRevenueFormatter heelRevenueFormatter = new HeelRevenueFormatter(false);
		
		int cost = 0;
		cost += baseFuelCostFormatter.getIntValue(object);
		cost += heelCostFormatter.getIntValue(object);
		cost += ballastBonusFormatter.getIntValue(object);
		cost += charterCostFormatter.getIntValue(object);
		cost += portCostFormatter.getIntValue(object);
		cost += repositioningFeeFormatter.getIntValue(object);

		if (withBOG) {
			cost += lngCostFormatter.getIntValue(object);
		}
		
		int revenue = 0;
		//FIXME: no generated for pinned element in diff mode!
		//revenue += generatedCharterRevenueFormatter.getIntValue(object);
		revenue += heelRevenueFormatter.getIntValue(object);
		
		final int total = cost - revenue;
		return total;
	}
}
