/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.ICostTypeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.NumberOfDPFormatter;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.Sequence;

public class CharterLengthDaysFormatter extends NumberOfDPFormatter implements ICostTypeFormatter {

	public CharterLengthDaysFormatter() {
		super(1);
	}

	@Override
	public Type getType() {
		return Type.OTHER;
	}

	@Override
	public Double getDoubleValue(final Object object) {
		int currentHours = SequenceGrabber.applyToSequences(object, this::getSequenceHours);
		return currentHours / 24.0;
	}

	protected int getSequenceHours(final Sequence sequence) {
		int time = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof GeneratedCharterLengthEvent) {
				time += evt.getDuration();
			}
		}
		return time;
	}
}
