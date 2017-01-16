/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * The {@link EventGroupPropertySource} is an {@link IPropertySource} to group the properties for like elements together. If we are looking for the properties for a Load Event, then also show
 * properties for the discharge and related voyages. This delegates to the {@link EventPropertySource} for the real property processing.
 * 
 * @author Simon Goodall
 * 
 */
public class EventGroupPropertySource implements IPropertySource {
	/**
	 * The target {@link Event}
	 */
	private final Event event;

	/**
	 * Internal mapping between a string form of the {@link Event#hashCode()} and the {@link Event}
	 */
	private final Map<String, Event> map = new WeakHashMap<String, Event>();

	public EventGroupPropertySource(final Event event) {
		this.event = event;
	}

	@Override
	public Object getEditableValue() {
		return event;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		final List<IPropertyDescriptor> descriptors = new ArrayList<IPropertyDescriptor>();

		// Iterate back to the start of the related elements, the iterate forward to the first unrelated element.

		Event start = event;

		// Find segment start
		while (start != null && !isSegmentStart(start)) {
			start = start.getPreviousEvent();
		}

		if (start == null) {
			// Unable to find related elements, just process this element.
			descriptors.add(createDescriptor(1, event));
			return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
		}

		// Add descriptors for each element until we reach the start of the next segment
		Event end = start;
		int idx = 1;
		do {
			// Create
			descriptors.add(createDescriptor(idx++, end));
			end = end.getNextEvent();
		} while (end != null && !isSegmentStart(end));

		return descriptors.toArray(new IPropertyDescriptor[descriptors.size()]);
	}

	protected IPropertyDescriptor createDescriptor(int sequence, final Event evt) {
		map.put(Integer.toString(evt.hashCode()), evt);

		String displayName = sequence + ". ";
		if (evt instanceof SlotVisit && ((SlotVisit) evt).getSlotAllocation().getSlot() instanceof LoadSlot) {
			displayName += "Load";
		} else if (evt instanceof SlotVisit && ((SlotVisit) evt).getSlotAllocation().getSlot() instanceof DischargeSlot) {
			displayName += "Discharge";
		} else if (evt instanceof VesselEventVisit) {
			VesselEventVisit vesselEventVisit = (VesselEventVisit) evt;
			displayName += vesselEventVisit.type();
		} else if (evt instanceof Cooldown) {
			displayName += "Cooldown";
		} else if (evt instanceof Journey) {
			Journey journey = (Journey) evt;
			displayName += "Journey " + (journey.isLaden() ? " (Laden)" : "(Ballast)");
		} else if (evt instanceof Idle) {
			Idle idle = (Idle) evt;
			displayName += "Idle " + (idle.isLaden() ? " (Laden)" : "(Ballast)");
		} else if (evt instanceof StartEvent) {
			displayName += "Start";
		} else if (evt instanceof EndEvent) {
			displayName += "End";
		} else if (evt instanceof GeneratedCharterOut) {
			displayName += "Generated Charter Out";
		} else {
			displayName += "Event";
		}

		final PropertyDescriptor propertyDescriptor = new PropertyDescriptor(Integer.toString(evt.hashCode()), displayName);
		return propertyDescriptor;
	}

	@Override
	public Object getPropertyValue(final Object id) {

		final Event evt = map.get(id);
		return new EventPropertySource(evt);
	}

	@Override
	public boolean isPropertySet(final Object id) {
		return true;
	}

	@Override
	public void resetPropertyValue(final Object id) {

	}

	@Override
	public void setPropertyValue(final Object id, final Object value) {

	}

	/**
	 * Returns true if the event is of a type to indicate the start of a segment of related events.
	 * 
	 * @param event
	 * @return
	 */
	private boolean isSegmentStart(final Event event) {
		if (event instanceof StartEvent) {
			return true;
		} else if (event instanceof EndEvent) {
			return true;
		} else if (event instanceof GeneratedCharterOut) {
			return true;
		} else if (event instanceof SlotVisit && ((SlotVisit) event).getSlotAllocation().getSlot() instanceof LoadSlot) {
			return true;
		} else if (event instanceof VesselEventVisit) {
			return true;
		}
		return false;
	}
}
