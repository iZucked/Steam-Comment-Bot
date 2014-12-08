package com.mmxlabs.lingo.reports.views.vertical;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.collect.Range;
import com.mmxlabs.common.Pair;
import com.mmxlabs.common.Triple;
import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ITimezoneProvider;

public abstract class AbstractVerticalReportVisualiser {

	public static RGB Black = new RGB(0, 0, 0);
	public static RGB Grey = new RGB(168, 168, 168);
	public static RGB Header_Grey = new RGB(228, 228, 228);
	public static RGB Light_Grey = new RGB(240, 240, 240);
	public static RGB Light_Orange = new RGB(255, 197, 168);
	public static RGB Orange = new RGB(255, 168, 64);
	protected final HashMap<RGB, Color> colourMap = new HashMap<>();

	protected final Map<Pair<EObject, EAttribute>, LocalDate> dateCacheA = new HashMap<>();
	protected final Map<Triple<EObject, Date, EAttribute>, LocalDate> dateCacheB = new HashMap<>();

	public DateTimeFormatter createDateFormat() {
		return DateTimeFormat.forPattern("dd/MMM/yy");
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

	public Color getColour(final RGB rgb) {
		if (colourMap.containsKey(rgb)) {
			return colourMap.get(rgb);
		} else {
			final Color result = new Color(Display.getCurrent(), rgb);
			colourMap.put(rgb, result);
			return result;
		}
	}

	public Color getEventBackgroundColor(final LocalDate date, final Event event) {

		if (event instanceof SlotVisit) {
			return getColorFor(date, (SlotVisit) event);
		}
		if (event instanceof Journey) {
			if (((Journey) event).isLaden()) {
				return getColour(ColourPalette.Vessel_Laden_Journey);
			} else
				return getColour(ColourPalette.Vessel_Ballast_Journey);
		}
		if (event instanceof CharterOutEvent) {
			return getColour(ColourPalette.Vessel_Charter_Out);
		}
		if (event instanceof GeneratedCharterOut) {
			return getColour(ColourPalette.Vessel_Generated_Charter_Out);
		}

		if (event instanceof Idle) {
			if (((Idle) event).isLaden()) {
				return getColour(ColourPalette.Vessel_Laden_Idle);
			} else
				return getColour(ColourPalette.Vessel_Ballast_Idle);
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
			final LocalDate eventStart = getLocalDateFor(event, SchedulePackage.Literals.EVENT__START);

			// how many days since the start of the event?
			int days = Days.daysBetween(eventStart, date).getDays();
			days += 1;
			return Integer.toString(days) + (days == 1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()) : "");
		}

		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			// True or false or both?
			if (date != null && isDayOutsideActualVisit(date, visit)) {
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
			return "Start";
		} else if (event instanceof EndEvent) {
			return "End";
		} else if (event instanceof Cooldown) {
			return "Cooldown";
		}

		final EClass eventClass = event.eClass();
		return eventClass.getName() + " '" + event.name();
	}

	public Color getSlotColour(final SlotVisit visit) {
		return getColour(Orange);
	}

	/**
	 * Default to port name
	 * 
	 * @param port
	 * @return
	 */
	public String getShortPortName(final Port port) {
		return port.getName();
	}

	public void dispose() {
		for (final Color colour : colourMap.values()) {
			colour.dispose();
		}
		colourMap.clear();
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

		final LocalDate eventStart = getLocalDateFor(visit, SchedulePackage.Literals.EVENT__START);
		final LocalDate eventEnd = getLocalDateFor(visit, SchedulePackage.Literals.EVENT__END);

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
		final LocalDate eventStart = getLocalDateFor(slot, slot.getWindowStartWithSlotOrPortTime(), CargoPackage.Literals.SLOT__WINDOW_START);
		final LocalDate eventEnd = getLocalDateFor(slot, slot.getWindowEndWithSlotOrPortTime(), CargoPackage.Literals.SLOT__WINDOW_START);

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
	public Event[] getEventsByWindowStart(final Sequence seq, final LocalDate date) {
		return getEventsByWindowStart(seq, date, date.plusDays(1));
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	public Event[] getEventsByWindowStart(final Sequence seq, final LocalDate start, final LocalDate end) {
		final List<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {

				final LocalDate eventStart;
				final LocalDate eventEnd;

				if (event instanceof SlotVisit) {
					final SlotVisit slotVisit = (SlotVisit) event;
					final Pair<LocalDate, LocalDate> p = getWindowDatesForSlotVisit(slotVisit);
					eventStart = p.getFirst();
					eventEnd = p.getSecond();
				} else if (event instanceof VesselEventVisit) {
					final VesselEventVisit vesselEventVisit = (VesselEventVisit) event;
					final VesselEvent vesselEvent = vesselEventVisit.getVesselEvent();
					eventStart = getLocalDateFor(vesselEvent, CargoPackage.Literals.VESSEL_EVENT__START_AFTER);
					eventEnd = getLocalDateFor(vesselEvent, CargoPackage.Literals.VESSEL_EVENT__START_BY);
				} else {
					eventStart = getLocalDateFor(event, SchedulePackage.Literals.EVENT__START);
					eventEnd = getLocalDateFor(event, SchedulePackage.Literals.EVENT__END);
				}
				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (eventStart.isAfter(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.isBefore(eventEnd)) {
					result.add(event);
				}
			}
		}
		return result.toArray(new Event[0]);
	}

	public Pair<LocalDate, LocalDate> getWindowDatesForSlotVisit(final SlotVisit slotVisit) {
		final Slot slot = slotVisit.getSlotAllocation().getSlot();
		final LocalDate eventStart = getLocalDateFor(slot, slot.getWindowStartWithSlotOrPortTime(), CargoPackage.Literals.SLOT__WINDOW_START);
		final LocalDate eventEnd = getLocalDateFor(slot, slot.getWindowEndWithSlotOrPortTime(), CargoPackage.Literals.SLOT__WINDOW_START);
		return new Pair<>(eventStart, eventEnd);
	}

	/**
	 * Returns all events in the specified sequence which overlap with the 24 hr period starting with the specified date
	 * 
	 * @param seq
	 * @param date
	 * @return
	 */
	public Event[] getEventsByScheduledDate(final Sequence seq, final LocalDate date) {
		return getEventsByScheduledDate(seq, date, date.plusDays(1));
	}

	/**
	 * Returns the events, if any, occurring between the two dates specified.
	 */
	public Event[] getEventsByScheduledDate(final Sequence seq, final LocalDate start, final LocalDate end) {
		final List<Event> result = new ArrayList<>();
		if (seq != null && start != null && end != null) {
			for (final Event event : seq.getEvents()) {

				final LocalDate eventStart = getLocalDateFor(event, SchedulePackage.Literals.EVENT__START);
				final LocalDate eventEnd = getLocalDateFor(event, SchedulePackage.Literals.EVENT__END);

				// when we get to an event after the search window, break the loop
				// NO: events are not guaranteed to be sorted by date :(
				if (eventStart.isAfter(end)) {
					// break;
				}
				// otherwise, as long as the event is in the search window, add it to the results
				// if the event ends at midnight, we do *not* count it towards this day
				else if (start.isBefore(eventEnd)) {
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

	public <T extends ITimezoneProvider & EObject> LocalDate getLocalDateFor(final T object, final EAttribute feature) {

		final Pair<EObject, EAttribute> key = new Pair<>((EObject) object, feature);
		if (dateCacheA.containsKey(key)) {
			return dateCacheA.get(key);
		} else {
			final LocalDate localDate = VerticalReportUtils.getLocalDateFor(object, feature, datesAreUTCEquivalent());
			dateCacheA.put(key, localDate);
			return localDate;
		}
	}

	public <T extends ITimezoneProvider & EObject> LocalDate getLocalDateFor(final T object, final Date date, final EAttribute feature) {
		final Triple<EObject, Date, EAttribute> key = new Triple<>((EObject) object, date, feature);
		if (dateCacheB.containsKey(key)) {
			return dateCacheB.get(key);
		} else {
			final LocalDate localDate = VerticalReportUtils.getLocalDateFor(object, date, feature, datesAreUTCEquivalent());
			dateCacheB.put(key, localDate);
			return localDate;
		}
	}

	public void inputChanged() {
		dateCacheA.clear();
		dateCacheB.clear();
	}
}
