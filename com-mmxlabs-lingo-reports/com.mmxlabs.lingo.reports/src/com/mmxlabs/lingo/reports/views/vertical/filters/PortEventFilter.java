/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical.filters;

import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Filter to filter out events which do not occur at one of a specified list of ports.
 * 
 * @author mmxlabs
 * 
 */
public class PortEventFilter extends FieldEventFilter<Port> {

	public PortEventFilter(@Nullable final EventFilter filter, @NonNull final List<Port> values) {
		super(filter, values);
	}

	public PortEventFilter(@NonNull final List<Port> values) {
		this(null, values);
	}

	@Override
	protected Port getEventField(@NonNull final Event event) {
		return event.getPort();
	}

}