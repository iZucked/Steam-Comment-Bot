/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Sequence;

public class HeelRevenueFormatter extends CostFormatter {

	public HeelRevenueFormatter(final boolean includeUnits) {
		super(includeUnits);
	}

	public HeelRevenueFormatter(final boolean includeUnits, final Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getHeelRevenue);
	}

	private int getHeelRevenue(final Sequence sequence) {
		int revenue = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof PortVisit) {
				final PortVisit portVisit = (PortVisit) evt;
				revenue += portVisit.getHeelRevenue();
			}
		}
		return revenue;
	}

}
