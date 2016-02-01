/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.util.List;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.impl.SequenceImpl;

public class VirtualSequence extends SequenceImpl {
	public VirtualSequence(final List<? extends Event> events) {
		super();
		this.getEvents().addAll(events);
	}
}