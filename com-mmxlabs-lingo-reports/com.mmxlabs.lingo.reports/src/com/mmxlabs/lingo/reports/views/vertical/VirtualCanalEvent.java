/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;

import com.mmxlabs.models.lng.cargo.CanalBookingSlot;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.impl.EventImpl;

public class VirtualCanalEvent extends EventImpl {
	private final CanalBookingSlot slot;
	private final boolean used;
	private String name;

	public VirtualCanalEvent(final CanalBookingSlot slot, String name, Port port, boolean used) {
		super();
		this.slot = slot;
		this.name = name;
		this.used = used;
		this.setStart(slot.getBookingDate().atStartOfDay(port.getZoneId()));
		this.setEnd(slot.getBookingDate().atStartOfDay(port.getZoneId()));
		this.setPort(port);
	}

	public LocalDate getDate() {
		return slot.getBookingDate();
	}

	@Override
	public String name() {
		return name;
	}

	public boolean isUsed() {
		return used;
	}
}
