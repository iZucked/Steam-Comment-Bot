/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Rectangle;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDayCanalJourneyAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDaySlotVisitLatenessAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDaySlotWindowAnnotation;
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
import com.mmxlabs.widgets.schedulechart.IScheduleChartSettings;
import com.mmxlabs.widgets.schedulechart.ScheduleCanvasState;
import com.mmxlabs.widgets.schedulechart.ScheduleEvent;
import com.mmxlabs.widgets.schedulechart.ScheduleEventAnnotation;
import com.mmxlabs.widgets.schedulechart.ScheduleEventSelectionState;
import com.mmxlabs.widgets.schedulechart.draw.DrawableElement;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEvent;
import com.mmxlabs.widgets.schedulechart.draw.DrawableScheduleEventAnnotation;
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
			if (sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0) {
				return sv.getSlotAllocation().getSlot() instanceof LoadSlot ? new LateLoadEvent(s, b, noneSelected) : new LateDischargeEvent(s, b, noneSelected);
			}
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

	@Override
	public DrawableScheduleEventAnnotation createDrawableScheduleEventAnnotation(ScheduleEventAnnotation sea, DrawableScheduleEvent dse, ToIntFunction<LocalDateTime> timeToXCoord, ScheduleCanvasState canvasState, IScheduleChartSettings settings) {
		if (!(sea.getData() instanceof NinetyDayScheduleEventAnnotationType)) {
			return null;
		}
		
		final NinetyDayScheduleEventAnnotationType type = (NinetyDayScheduleEventAnnotationType) sea.getData();
		return switch (type) {
		case SLOT_WINDOW -> new NinetyDaySlotWindowAnnotation(sea, dse, settings, timeToXCoord);
		case LATENESS_BAR -> new NinetyDaySlotVisitLatenessAnnotation(sea, dse, settings, timeToXCoord);
		case CANAL_JOURNEY -> new NinetyDayCanalJourneyAnnotation(sea, dse, settings, timeToXCoord);
		default ->
			throw new IllegalArgumentException("Unexpected value: " + sea.getData());
		};
	}

	@Override
	public Comparator<DrawableElement> getEventAndAnnotationRenderOrderComparator() {
		return (de1, de2) -> {
			int de1Depth = getRenderOrderRelativeToEvent(de1);
			int de2Depth = getRenderOrderRelativeToEvent(de2);
			if (de1Depth != de2Depth) {
				return Integer.compare(de1Depth, de2Depth);
			}
			
			ScheduleEventSelectionState de1SelectionState = getSelectionState(de1);
			ScheduleEventSelectionState de2SelectionState = getSelectionState(de2);
			if (!Objects.equal(de1SelectionState, de2SelectionState)) {
				return (de1SelectionState == null) ? -1 : de1SelectionState.compareTo(de2SelectionState);
			}
			
			if (de1 instanceof DrawableScheduleEvent dse1 && de2 instanceof DrawableScheduleEvent dse2) {
				int res = getScheduleEventRenderOrderComparator().compare(dse1.getScheduleEvent(), dse2.getScheduleEvent());
				return res;
			}
			
			return 0;
		};
	}
	
	private Comparator<ScheduleEvent> getScheduleEventRenderOrderComparator() {
		return (se1, se2) -> {
			Object o1 = se1.getData();
			Object o2 = se2.getData();

			if (o1.getClass().equals(o2.getClass())) {
				return 0;
			}
			
			return (o1 instanceof SlotVisit) ? -1 : (o2 instanceof SlotVisit) ? 1 : 0;
		};
	}
	
	private ScheduleEventSelectionState getSelectionState(DrawableElement eventOrAnnotation) {
		if (eventOrAnnotation instanceof DrawableScheduleEvent dse) {
			return dse.getScheduleEvent().getSelectionState();
		} else if (eventOrAnnotation instanceof DrawableScheduleEventAnnotation dsea) {
			return dsea.getScheduleEvent().getSelectionState();
		}
		return null;
	}
	
	private int getRenderOrderRelativeToEvent(DrawableElement de) {
		if (de instanceof DrawableScheduleEvent) {
			return 0;
		} else if (de instanceof DrawableScheduleEventAnnotation dsea) {
			NinetyDayScheduleEventAnnotationType type = (NinetyDayScheduleEventAnnotationType) dsea.getAnnotation().getData();
			return type.isRenderedBeforeEvent() ? -1 : 1;
		}
		return 0;
	}

}
