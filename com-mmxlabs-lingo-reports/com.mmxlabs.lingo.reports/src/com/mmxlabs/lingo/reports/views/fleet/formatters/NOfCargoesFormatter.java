/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import com.mmxlabs.lingo.reports.views.formatters.ICostTypeFormatter;
import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class NOfCargoesFormatter extends IntegerFormatter implements ICostTypeFormatter {

	@Override
	public Type getType() {
		return Type.OTHER;
	}

	@Override
	public Integer getIntValue(final Object object) {
		return SequenceGrabber.applyToSequences(object, this::getNOfCargoes);
	}

	private int getNOfCargoes(final Sequence sequence) {
		int nOfCargoes = 0;
		CargoAllocation lastAllocation = null;
		for (final Event evt : sequence.getEvents()) {
			if (evt instanceof SlotVisit) {
				final SlotVisit slotVisit = (SlotVisit) evt;
				final SlotAllocation sa = slotVisit.getSlotAllocation();
				if (sa != null) {
					final CargoAllocation ca = sa.getCargoAllocation();
					if (lastAllocation != ca) {
						nOfCargoes++;
						lastAllocation = ca;
					}
				}
			}
		}
		return nOfCargoes;
	}
}
