/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RepositioningFeeFormatter extends CostFormatter {

	public RepositioningFeeFormatter(final boolean includeUnits) {
		super(includeUnits);
	}

	public RepositioningFeeFormatter(final boolean includeUnits, final Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getRepositioningFee);
	}

	private int getRepositioningFee(final Sequence sequence) {
		int repositioningFee = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof StartEvent) {
				final StartEvent startEvent = (StartEvent) evt;
				repositioningFee += startEvent.getRepositioningFee();
			} else {
				if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
					if (vesselEventVisit.getVesselEvent() instanceof CharterOutEvent) {
						final CharterOutEvent charterOutEvent = (CharterOutEvent) vesselEventVisit.getVesselEvent();
						repositioningFee -= charterOutEvent.getRepositioningFee();
					}
				}
			}
		}
		return repositioningFee;
	}

}
