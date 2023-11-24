/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.ninetydayschedule;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.ToIntFunction;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.swt.graphics.Rectangle;

import com.google.common.base.Objects;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDayCanalJourneyAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDayCanalJourneyDefaultAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDaySlotVisitLatenessAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDaySlotVisitLatenessDefaultAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.annotations.NinetyDaySlotWindowAnnotation;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BalastIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.BalastJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.CharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.DischargeEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.DryDockEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.EndEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterLengthEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.GeneratedCharterOutEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenIdleEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LadenJourneyEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LateDischargeEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LateLoadEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.LoadEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.MaintenanceEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.NinetyDayPlaceholderEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.StartEvent;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSeqenceElements;
import com.mmxlabs.lingo.reports.views.ninetydayschedule.events.buysell.PositionsSequenceElement;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.NonShippedJourney;
import com.mmxlabs.models.lng.schedule.NonShippedSlotVisit;
import com.mmxlabs.models.lng.schedule.PortVisit;
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
	public @Nullable DrawableScheduleEvent createDrawableScheduleEvent(ScheduleEvent scheduleEvent, Rectangle bounds, ScheduleCanvasState canvasState) {
		final boolean noneSelected = canvasState != null ? canvasState.getSelectedEvents().isEmpty() : true;
		final Object data = scheduleEvent.getData();
		
		if (data instanceof final PositionsSequenceElement positionsSequenceElement) {
			return PositionsSeqenceElements.createDrawableScheduleEventFromPositionSequence(scheduleEvent, bounds, noneSelected, positionsSequenceElement);
		}
		if (data instanceof final Event event) {
			if (event instanceof final Journey j) {
				return j.isLaden() ? new LadenJourneyEvent(scheduleEvent, bounds, noneSelected) : new BalastJourneyEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof NonShippedJourney i) {
				return i.isLaden() ? new LadenJourneyEvent(scheduleEvent, bounds, noneSelected) : new BalastJourneyEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof Idle i) {
				return i.isLaden() ? new LadenIdleEvent(scheduleEvent, bounds, noneSelected) : new BalastIdleEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof SlotVisit sv) {
				if (sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0) {
					return sv.getSlotAllocation().getSlot() instanceof LoadSlot ? new LateLoadEvent(scheduleEvent, bounds, noneSelected) : new LateDischargeEvent(scheduleEvent, bounds, noneSelected);
				}
				return sv.getSlotAllocation().getSlot() instanceof LoadSlot ? new LoadEvent(scheduleEvent, bounds, noneSelected) : new DischargeEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof NonShippedSlotVisit sv) {
				if (sv.getLateness() != null && sv.getLateness().getLatenessInHours() > 0) {
					return sv.getSlot() instanceof LoadSlot ? new LateLoadEvent(scheduleEvent, bounds, noneSelected) : new LateDischargeEvent(scheduleEvent, bounds, noneSelected);
				}
				return sv.getSlot() instanceof LoadSlot ? new LoadEvent(scheduleEvent, bounds, noneSelected) : new DischargeEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof VesselEventVisit vev) {
				final VesselEvent ve = vev.getVesselEvent();
				if (ve instanceof com.mmxlabs.models.lng.cargo.DryDockEvent) {
					return new DryDockEvent(scheduleEvent, bounds, noneSelected);
				} else if (ve instanceof com.mmxlabs.models.lng.cargo.CharterOutEvent) {
					return new CharterOutEvent(scheduleEvent, bounds, noneSelected);
				} else if (ve instanceof com.mmxlabs.models.lng.cargo.MaintenanceEvent) {
					return new MaintenanceEvent(scheduleEvent, bounds, noneSelected);
				} else if (ve instanceof com.mmxlabs.models.lng.cargo.CharterLengthEvent) {
					return new CharterLengthEvent(scheduleEvent, bounds, noneSelected);
				}
			} else if (event instanceof GeneratedCharterOut) {
				return new GeneratedCharterOutEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent) {
				return new GeneratedCharterLengthEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof com.mmxlabs.models.lng.schedule.StartEvent) {
				return new StartEvent(scheduleEvent, bounds, noneSelected);
			} else if (event instanceof com.mmxlabs.models.lng.schedule.EndEvent) {
				return new EndEvent(scheduleEvent, bounds, noneSelected);
			}
			return new NinetyDayPlaceholderEvent(scheduleEvent, bounds, noneSelected);
		}
		return null;
	}

	@Override
	public DrawableScheduleEventAnnotation createDrawableScheduleEventAnnotation(ScheduleEventAnnotation sea, DrawableScheduleEvent dse, ToIntFunction<LocalDateTime> timeToXCoord, ScheduleCanvasState canvasState, IScheduleChartSettings settings) {
		if (sea.getData() instanceof final NinetyDayScheduleEventAnnotationType type) {
			if (settings.showAnnotations()) {
				return switch (type) {
				case SLOT_WINDOW -> new NinetyDaySlotWindowAnnotation(sea, dse, settings, timeToXCoord);
				case LATENESS_BAR -> new NinetyDaySlotVisitLatenessAnnotation(sea, dse, settings, timeToXCoord);
				case CANAL_JOURNEY -> new NinetyDayCanalJourneyAnnotation(sea, dse, settings, timeToXCoord);
				default -> throw new IllegalArgumentException("Unexpected value: " + sea.getData());
				};
			}
			else {
				return switch (type) {
				case LATENESS_BAR -> new NinetyDaySlotVisitLatenessDefaultAnnotation(sea, dse, settings, timeToXCoord);
				case CANAL_JOURNEY -> new NinetyDayCanalJourneyDefaultAnnotation(sea, dse, settings, timeToXCoord);
				default -> null;
				};
			}
		}
		return null;
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
			
			// If both or neither of events are port visits
			if(!((o1 instanceof PortVisit) ^ (o2 instanceof PortVisit))) {
				return 0;
			}
						
			return (o1 instanceof PortVisit) ? 1 : -1;
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
