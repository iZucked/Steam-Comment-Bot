/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterRevenueFormatter extends CostFormatter {

	public GeneratedCharterRevenueFormatter(Type type) {
		super(type);
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getSequenceRevenue);
	}

	protected int getSequenceRevenue(final Sequence sequence) {
		int revenue = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof GeneratedCharterOut) {
				revenue += ((GeneratedCharterOut) evt).getRevenue();
			}
		}
		return revenue;
	}
}
