/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.vertical;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import com.google.common.collect.Range;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.common.time.Days;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.lingo.reports.ColourPalette.ColourElements;
import com.mmxlabs.lingo.reports.ColourPalette.ColourPaletteItems;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public abstract class AbstractVerticalReportVisualiser {

	// public static RGB Black = new RGB(0, 0, 0);
	// public static RGB Grey = new RGB(168, 168, 168);
	// public static RGB Header_Grey = new RGB(228, 228, 228);
	// public static RGB Light_Grey = new RGB(240, 240, 240);
	// public static RGB Light_Orange = new RGB(255, 197, 168);
	// public static RGB Orange = new RGB(255, 168, 64);

	protected final Map<Pair<EObject, EAttribute>, LocalDateTime> dateCacheA = new HashMap<>();
	protected final Map<Triple<EObject, Date, EAttribute>, LocalDateTime> dateCacheB = new HashMap<>();

	protected final ColourPalette colourPalette;

	protected AbstractVerticalReportVisualiser() {
		this(ColourPalette.getInstance());
	}

	protected AbstractVerticalReportVisualiser(final ColourPalette colourPalette) {
		this.colourPalette = colourPalette;
	}

	public DateTimeFormatter createDateFormat() {
		return DateTimeFormatter.ofPattern("dd/MMM/yy");
	}

	public Color getColorFor(final LocalDate date, final SlotVisit visit) {
		// final SlotAllocation allocation = visit.getSlotAllocation();
		// final boolean isWindow = VerticalReportUtils.isDayOutsideActualVisit(date, visit);

		// if (allocation != null) {
		// final Slot slot = allocation.getSlot();
		// if (slot != null) {
		// final Cargo cargo = slot.getCargo();
		// // if (cargo != null && cargo.isAllowRewiring() == false) {
		// // return isWindow ? getColour(Light_Grey) : getColour(Grey);
		// // }
		// }
		// }
		return /* isWindow ? getColour(Light_Orange) : */getSlotColour(visit);
	}

	public Color getEventBackgroundColor(final LocalDate date, final Event event) {

		if (event instanceof SlotVisit) {
			return getColorFor(date, (SlotVisit) event);
		}
		if (event instanceof Journey) {
			if (((Journey) event).isLaden()) {
				return colourPalette.getColourFor(ColourPaletteItems.Voyage_Laden_Journey, ColourElements.Background);
			} else {
				return colourPalette.getColourFor(ColourPaletteItems.Voyage_Ballast_Journey, ColourElements.Background);
			}
		}

		if (event instanceof VesselEventVisit) {
			final VesselEvent vesselEvent = ((VesselEventVisit) event).getVesselEvent();

			if (vesselEvent instanceof CharterOutEvent) {
				return colourPalette.getColourFor(ColourPaletteItems.Event_CharterOut, ColourElements.Background);
			}
			if (vesselEvent instanceof DryDockEvent) {
				return colourPalette.getColourFor(ColourPaletteItems.Event_DryDock, ColourElements.Background);
			}
			if (vesselEvent instanceof MaintenanceEvent) {
				return colourPalette.getColourFor(ColourPaletteItems.Event_Maintenence, ColourElements.Background);
			}
		}
		if (event instanceof GeneratedCharterOut) {
			return colourPalette.getColourFor(ColourPaletteItems.Voyage_GeneratedCharterOut, ColourElements.Background);
		}

		if (event instanceof Idle) {
			if (((Idle) event).isLaden()) {
				return colourPalette.getColourFor(ColourPaletteItems.Voyage_Laden_Idle, ColourElements.Background);
			} else
				return colourPalette.getColourFor(ColourPaletteItems.Voyage_Ballast_Idle, ColourElements.Background);
		}

		return null;

	}

	public Font getEventFont(final LocalDate element, final Event event) {
		return null;
	}

	abstract public Color getEventForegroundColor(LocalDate element, Event event);

	public String getEventText(final LocalDate date, final Event event) {
		if (date == null || event == null) {
			return "";
		}

		// Journey events just show the day number
		if (event instanceof Journey) {
			final LocalDate eventStart = getLocalDateFor(event.getStart());

			// how many days since the start of the event?
			int days = Days.between(eventStart, date);
			days += 1;
			return Integer.toString(days) + (days == 1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()) : "");
		}

		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			// True or false or both?
			if (isDayOutsideActualVisit(date, visit)) {
				return "";
			}
			String result = getShortPortName(visit.getPort());

			final SlotAllocation allocation = visit.getSlotAllocation();
			if (allocation != null) {
				final Slot slot = allocation.getSlot();
				if (slot != null) {
					result += " " + slot.getName();
				}
			}

			return result;
		} else if (event instanceof Idle) {
			return "";
		} else if (event instanceof VesselEventVisit) {
			final VesselEvent vesselEvent = ((VesselEventVisit) event).getVesselEvent();
			if (vesselEvent instanceof CharterOutEvent) {
				return "CO";
			} else if (vesselEvent instanceof DryDockEvent) {
				return "Dry Dock";
			} else if (vesselEvent instanceof MaintenanceEvent) {
				return "Maintenance";
			}

		} else if (event instanceof GeneratedCharterOut) {
			return "GCO";
		} else if (event instanceof StartEvent) {
			final Port port = event.getPort();
			if (port != null) {
				return String.format("Start - %s", getShortPortName(port));
			} else {
				return "Start";
			}
		} else if (event instanceof EndEvent) {
			final Port port = event.getPort();
			if (port != null) {
				return String.format("End - %s", getShortPortName(port));
			} else {
				return "End";
			}
		} else if (event instanceof Cooldown) {
			return "Cooldown";
		}

		final EClass eventClass = event.eClass();
		return eventClass.getName() + " '" + event.name();
	}

	public Color getSlotColour(final SlotVisit visit) {
		if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
			return colourPalette.getColourFor(ColourPaletteItems.Voyage_Load, ColourElements.Background);
		} else {
			return colourPalette.getColourFor(ColourPaletteItems.Voyage_Discharge, ColourElements.Background);
		}
	}

	/**
	 * Default to port name
	 * 
	 * @param port
	 * @return
	 */
	public String getShortPortName(final Port port) {
		if (port.getShortName() != null && !port.getShortName().isEmpty()) {
			return port.getShortName();
		}
		return port.getName();
	}

	public void dispose() {
	}

	/**
	 * Is the specified day outside of the actual slot visit itself? (A SlotVisit event can be associated with a particular day if its slot window includes that day.)
	 * 
	 * @param day
	 * @param visit
	 * @return
	 */
	public boolean isDayOutsideActualVisit(final LocalDate day, final SlotVisit visit) {
		final LocalDate nextDay = day.plusDays(1);

		final LocalDate eventStart = getLocalDateFor(visit.getStart());
		final LocalDate eventEnd = getLocalDateFor(visit.getEnd());

		return (nextDay.isBefore(eventStart) || (eventEnd.isAfter(day) == false));
	}

	/**
	 * Is the specified day within the slot visit window?
	 * 
	 * @param day
	 * @param visit
	 * @return
	 */
	public boolean isDayInsideWindow(final LocalDate day, final SlotVisit visit) {
		final Slot slot = visit.getSlotAllocation().getSlot();
		final LocalDate eventStart = getLocalDateFor(slot.getWindowStartWithSlotOrPortTime());
		final LocalDate eventEnd = getLocalDateFor(slot.getWindowEndWithSlotOrPortTime());

		final Range<LocalDate> range = Range.closedOpen(eventStart, eventEnd);
		return range.contains(day);
	}

	/**
	 * Returns all events in the specified sequence which overlap with the 24 hr period starting with the specified date
	 * 
	 * @param seq
	 * @param date
	 * @return
	 */
	public @NonNull Event[] getEventsByScheduledDate(final Sequence seq, final LocalDate date) {
		return getEventsByScheduledDate(seq, date, date.plusDays(1));
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 * 
	 */
	public @NonNull Event[] getEventsByScheduledDate(final Sequence seq, final LocalDate start, final LocalDate end) {
		final List<@NonNull Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {

				final LocalDate eventStart = getLocalDateFor(event.getStart());
				final LocalDate eventEnd = getLocalDateFor(event.getEnd());

				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (eventStart.isAfter(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.equals(eventStart)) {
					result.add(event);
				} else if (!start.isBefore(eventStart) && start.isBefore(eventEnd)) {
					result.add(event);
				}
			}
		}
		return result.toArray(new Event[0]);
	}

	/**
	 * Return true if dates should be rendered in local time - i.e. "UTC equivalent" (10 AM local time is also 10 AM UTC) or as actual UTC time (thus 10 AM localtime is not necessarily 10 AM UTC).
	 * 
	 * @return
	 */
	public boolean datesAreUTCEquivalent() {
		return false;
	}

	// public <T extends ITimezoneProvider & EObject> LocalDate getLocalDateFor(final T object, final EAttribute feature) {
	// return getLocalDateTimeFor(object, feature).toLocalDate();
	// }
	//
	// public <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateTimeFor(final T object, final EAttribute feature) {
	//
	// final Pair<EObject, EAttribute> key = new Pair<>((EObject) object, feature);
	// if (dateCacheA.containsKey(key)) {
	// return dateCacheA.get(key);
	// } else {
	// final LocalDateTime localDate = VerticalReportUtils.getLocalDateFor(object, feature, datesAreUTCEquivalent());
	// dateCacheA.put(key, localDate);
	// return localDate;
	// }
	// }
	//
	// public <T extends ITimezoneProvider & EObject> LocalDate getLocalDateFor(final T object, final Date date, final EAttribute feature) {
	// return getLocalDateTimeFor(object, date, feature).toLocalDate();
	// }
	//
	// public <T extends ITimezoneProvider & EObject> LocalDateTime getLocalDateTimeFor(final T object, final Date date, final EAttribute feature) {
	// final Triple<EObject, Date, EAttribute> key = new Triple<>((EObject) object, date, feature);
	// if (dateCacheB.containsKey(key)) {
	// return dateCacheB.get(key);
	// } else {
	// final LocalDateTime localDate = VerticalReportUtils.getLocalDateFor(object, date, feature, datesAreUTCEquivalent());
	// dateCacheB.put(key, localDate);
	// return localDate;
	// }
	// }

	@NonNull
	public LocalDate getLocalDateFor(@NonNull final ZonedDateTime dateTime) {
		if (datesAreUTCEquivalent()) {
			return dateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate();
		}
		return dateTime.toLocalDate();
	}

	@NonNull
	public LocalDateTime getLocalDateTimeFor(@NonNull final ZonedDateTime dateTime) {
		if (datesAreUTCEquivalent()) {
			return dateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
		}
		return dateTime.toLocalDateTime();
	}

	public void inputChanged() {
		dateCacheA.clear();
		dateCacheB.clear();
	}

	public ColourPalette getColourPalette() {
		return colourPalette;
	}

	public enum Alignment {
		LEFT, RIGHT, CENTRE
	}

	public Alignment getEventTextAlignment(final LocalDate localDate, final Event event) {
		return Alignment.LEFT;
	}

	/**
	 * Flag indicating whether or not to strip out rows after last interesting event. TODO: Add API to define interesting
	 * 
	 * @return
	 */
	public boolean filterAfterLastEvent() {
		return false;
	}
}
