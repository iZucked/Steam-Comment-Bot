/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Rectangle;

import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BalastIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BallastJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.DischargeEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.DryDockEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LoadEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.MaintenanceEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.NinetyDayPlaceholderEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.providers.IDrawableScheduleEventProvider;

public class NinetyDayDrawableEventProvider implements IDrawableScheduleEventProvider {

	@Override
	public @Nullable DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent s, Rectangle b, ScheduleCanvasState canvasState) {
		if (!(s.getData() instanceof Event)) {
			return null;
		}
		Event e = (Event) s.getData();
		boolean noneSelected = canvasState.getSelectedEvents().isEmpty();

		if (e instanceof final Journey j) {
			return j.isLaden() ? new LadenJourneyEvent(s, b, noneSelected) : new BallastJourneyEvent(s, b, noneSelected);
		} else if (e instanceof Idle i) {
			return i.isLaden() ? new LadenIdleEvent(s, b, noneSelected) : new BalastIdleEvent(s, b, noneSelected);
		} else if (e instanceof SlotVisit sv) {
			return sv.getSlotAllocation().getSlot() instanceof LoadSlot ? new LoadEvent(s, b, noneSelected) : new DischargeEvent(s, b, noneSelected);
		} else if (e instanceof VesselEventVisit vev) {
			final VesselEvent ve = vev.getVesselEvent();
			if (ve instanceof com.mmxlabs.models.lng.cargo.DryDockEvent) {
				return new DryDockEvent(s, b, noneSelected);
			} else if (ve instanceof com.mmxlabs.models.lng.cargo.CharterOutEvent) {
				return new CharterOutEvent(s, b, noneSelected);
			} else if (ve instanceof com.mmxlabs.models.lng.cargo.MaintenanceEvent) {
				return new MaintenanceEvent(s, b, noneSelected);
			} else if (ve instanceof com.mmxlabs.models.lng.cargo.CharterLengthEvent) {
				return new CharterLengthEvent(s, b, noneSelected);
			}
		} else if (e instanceof GeneratedCharterOut) {
			return new GeneratedCharterOutEvent(s, b, noneSelected);
		} else if (e instanceof com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent) {
			return new GeneratedCharterLengthEvent(s, b, noneSelected);
		}
		return new NinetyDayPlaceholderEvent(s, b, noneSelected);
	}

}
