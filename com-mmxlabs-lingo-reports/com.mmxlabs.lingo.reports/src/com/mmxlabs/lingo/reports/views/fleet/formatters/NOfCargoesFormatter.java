/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.fleet.formatters;

import java.util.List;

import com.mmxlabs.lingo.reports.views.formatters.IntegerFormatter;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class NOfCargoesFormatter extends IntegerFormatter  {

	@Override
	public Integer getIntValue(Object object) {

		if (object instanceof Row) {
			object = ((Row) object).getSequence();
		}
		int nOfCargoes = 0;
		if (object instanceof Sequence) {
			Sequence sequence = (Sequence) object;
			nOfCargoes += getNOfCargoes(sequence);
		} else if (object instanceof List) {
			List<?> objects = (List<?>) object;
			if (objects.size() > 0) {
				for (Object o : objects) {
					if (o instanceof Sequence) {
						nOfCargoes += getNOfCargoes((Sequence) o);
					}
				}
			}
		}

		return nOfCargoes;
	}

	private int getNOfCargoes(Sequence sequence) {
		int nOfCargoes = 0;
		for (Event evt : sequence.getEvents()) {
			if (evt instanceof SlotVisit) {
				nOfCargoes++;
			}
		}
		//Divided by 2 as we have load slots and discharge slots.
		return nOfCargoes / 2;
	}
}
