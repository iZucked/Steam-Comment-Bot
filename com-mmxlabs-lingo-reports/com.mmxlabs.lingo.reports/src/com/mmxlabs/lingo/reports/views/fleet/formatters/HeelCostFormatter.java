/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;

public class HeelCostFormatter extends CostFormatter {

	public HeelCostFormatter(boolean includeUnits) {
		super(includeUnits);
	}

	public HeelCostFormatter(boolean includeUnits, Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(Object object) {

		return SequenceGrabber.applyToSequences(object, this::getHeelCost);
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
