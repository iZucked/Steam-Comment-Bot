/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.CostFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public class RepositioningFeeFormatter extends CostFormatter {

	public RepositioningFeeFormatter(boolean includeUnits) {
		super(includeUnits);
	}

	public RepositioningFeeFormatter(boolean includeUnits, Type type) {
		super(includeUnits, type);
	}

	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int repositioningFee = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			repositioningFee += getRepositioningFee(sequence);
		} else if (object instanceof List) {
			List objects = (List) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
						repositioningFee += getRepositioningFee((Sequence) o);
					}
				}
			}
		}

		return repositioningFee;
	}

	private int getRepositioningFee(Sequence sequence) {
		int repositioningFee = 0;
		for (Event evt : sequence.getEvents()) {
			if (evt instanceof StartEvent) {
				StartEvent startEvent = (StartEvent) evt;
				repositioningFee += startEvent.getRepositioningFee();
			} else {
				if (evt instanceof VesselEventVisit) {
					VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
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
