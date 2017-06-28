package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.schedule.impl.EventImpl;

public class VirtualCanalEvent extends EventImpl {
	private final CanalBookingSlot slot;
	private final boolean used;

	public VirtualCanalEvent(final CanalBookingSlot slot, boolean used) {
		super();
		this.slot = slot;
		this.used = used;
		this.setStart(slot.getBookingDateAsDateTime());
		this.setEnd(slot.getBookingDateAsDateTime());
		this.setPort(slot.getEntryPoint().getPort());
	}

	public LocalDate getDate() {
		return slot.getBookingDate();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return slot.getEntryPoint().getName();
	}

	public boolean isUsed() {
		return used;
	}
}
