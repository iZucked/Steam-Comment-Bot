package com.mmxlabs.lingo.reports.views.vertical;

import java.util.List;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;

/**
 * Filter to filter out events which do not occur at one of a specified list of ports.
 * 
 * @author mmxlabs
 * 
 */
public class PortEventFilter extends FieldEventFilter<Port> {

	public PortEventFilter(final EventFilter filter, final List<Port> values) {
		super(filter, values);
	}

	public PortEventFilter(final List<Port> values) {
		this(null, values);
	}

	@Override
	Port getEventField(final Event event) {
		return event.getPort();
	}

}