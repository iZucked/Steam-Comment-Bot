/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SlotVisit;

public class DESPurchaseSlotEventFilter extends BaseEventFilter {

	private boolean include;

	public DESPurchaseSlotEventFilter(final boolean include, final EventFilter filter) {
		super(filter);
		this.include = include;
	}

	@Override
	protected boolean isEventDirectlyFiltered(final LocalDate date, final Event event) {

		// Filter out any DES purchases.
		if (event instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) event;
			final Slot slot = slotVisit.getSlotAllocation().getSlot();
			if (slot instanceof LoadSlot) {
				final LoadSlot loadSlot = (LoadSlot) slot;
				if (loadSlot.isDESPurchase()) {
					// If include == true, include every DES purchase event
					return !include;
				}
			}
		}
		// If include == true, filter out every non DES purchase event
		return include;
	}
}
