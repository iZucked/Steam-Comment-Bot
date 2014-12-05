package com.mmxlabs.lingo.reports.views.vertical;

import java.util.HashMap;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
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
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

public abstract class AbstractVerticalReportVisualiser {

	public static RGB Black = new RGB(0, 0, 0);
	public static RGB Grey = new RGB(168, 168, 168);
	public static RGB Header_Grey = new RGB(228, 228, 228);
	public static RGB Light_Grey = new RGB(240, 240, 240);
	public static RGB Light_Orange = new RGB(255, 197, 168);
	public static RGB Orange = new RGB(255, 168, 64);
	protected final HashMap<RGB, Color> colourMap = new HashMap<>();

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
			LocalDate eventStart = VerticalReportUtils.getLocalDateFor(event.getStart(), TimeZone.getTimeZone(event.getTimeZone(SchedulePackage.Literals.EVENT__START)), false);
			
			// how many days since the start of the event?
			int days = Days.daysBetween(eventStart, date).getDays();
			days += 1;
			return Integer.toString(days) + (days == 1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()) : "");
		}

		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			// True or false or both?
			if (date != null && VerticalReportUtils.isDayOutsideActualVisit(date, visit, false)) {
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
		return eventClass.getName() + " '" + event.name();/// + "' " + Integer.toString(days);
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

}
