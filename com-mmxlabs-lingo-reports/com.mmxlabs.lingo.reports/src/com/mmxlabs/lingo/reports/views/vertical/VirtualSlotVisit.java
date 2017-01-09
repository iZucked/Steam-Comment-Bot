/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl;

/**
 * Virtual "Event" class for additional data which isn't organised in a Sequence.
 * 
 */
public class VirtualSlotVisit extends SlotVisitImpl {
	public VirtualSlotVisit(final Slot slot) {
		super();
		this.setStart(slot.getWindowStartWithSlotOrPortTime());
		this.setEnd(slot.getWindowEndWithSlotOrPortTime());
		this.setPort(slot.getPort());
		final SlotAllocation sa = ScheduleFactory.eINSTANCE.createSlotAllocation();
		sa.setSlotAllocationType(slot instanceof LoadSlot ? SlotAllocationType.PURCHASE : SlotAllocationType.SALE);
		sa.setSlot(slot);
		sa.setSlotVisit(this);
		this.setSlotAllocation(sa);
	}
}
