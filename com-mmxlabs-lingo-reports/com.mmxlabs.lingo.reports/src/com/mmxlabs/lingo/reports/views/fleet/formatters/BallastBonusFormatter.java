/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class BallastBonusFormatter extends CostFormatter {

	public BallastBonusFormatter(boolean includeUnits) {
		super(includeUnits);
	}
	
	public BallastBonusFormatter(boolean includeUnits, Type type) {
		super(includeUnits, type);
	}
	
	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int ballastBonus = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			ballastBonus += getBallastBonus(sequence);
		} else if (object instanceof List) {
			List objects = (List) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
					ballastBonus += getBallastBonus((Sequence) o);
					}
				}
			}
		}

		return ballastBonus;
	}

	private int getBallastBonus(Sequence sequence) {
		int revenue = 0;
		for (Event evt : sequence.getEvents()) {
			if (evt instanceof EndEvent) {
				final EndEvent endEvent = (EndEvent) evt;
				revenue -= endEvent.getBallastBonusFee();
			} else if (evt instanceof VesselEventVisit) {
				VesselEventVisit cargoAllocation = (VesselEventVisit) evt;
				if (cargoAllocation.getVesselEvent() instanceof CharterOutEvent) {
					final CharterOutEvent charterOutEvent = (CharterOutEvent) cargoAllocation.getVesselEvent();
					revenue += charterOutEvent.getBallastBonus();
				}
			}
		}
		return revenue;
	}

}
