/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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

public class HeelCostFormatter extends CostFormatter {

	public HeelCostFormatter(boolean includeUnits) {
		super(includeUnits);
	}
	
	public HeelCostFormatter(boolean includeUnits, Type type ) {
		super(includeUnits, type);
	}
	
	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int heelCost = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			heelCost += getHeelCost(sequence);
		} else if (object instanceof List) {
			List objects = (List) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
					heelCost += getHeelCost((Sequence) o);
					}
				}
			}
		}

		return heelCost;
	}

	private int getHeelCost(Sequence sequence) {
		int revenue = 0;
		for (Event evt : sequence.getEvents()) {
			if (evt instanceof PortVisit) {
				final PortVisit portVisit = (PortVisit) evt;
				revenue += portVisit.getHeelCost();
			}
		}
		return revenue;
	}

}
