/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class BallastBonusFormatter extends CostFormatter {

	public BallastBonusFormatter(final boolean includeUnits) {
		super(includeUnits);
	}

	public BallastBonusFormatter(final boolean includeUnits, final Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(final Object object) {

		return SequenceGrabber.applyToSequences(object, this::getBallastBonus);
	}

	private int getBallastBonus(final Sequence sequence) {
		int revenue = 0;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof EndEvent) {
				final EndEvent endEvent = (EndEvent) evt;
				revenue -= endEvent.getBallastBonusFee();
			} else if (evt instanceof VesselEventVisit) {
				final VesselEventVisit cargoAllocation = (VesselEventVisit) evt;
				if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
					final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
					revenue += charterOutEvent.getBallastBonus();
				}
			}
		}
		return revenue;
	}

}
