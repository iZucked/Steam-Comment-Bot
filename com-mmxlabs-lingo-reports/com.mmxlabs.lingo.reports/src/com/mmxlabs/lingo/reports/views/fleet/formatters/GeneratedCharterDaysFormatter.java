/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Sequence;

public class GeneratedCharterDaysFormatter extends NumberOfDPFormatter {

	public GeneratedCharterDaysFormatter() {
		super(1);

	}

	@Override
	public Double getDoubleValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getSequenceHours) / 24.0;
	}

	protected int getSequenceHours(final Sequence sequence) {
		int time = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof GeneratedCharterOut) {
				time += evt.getDuration();
			}
		}
		return time;
	}
}
