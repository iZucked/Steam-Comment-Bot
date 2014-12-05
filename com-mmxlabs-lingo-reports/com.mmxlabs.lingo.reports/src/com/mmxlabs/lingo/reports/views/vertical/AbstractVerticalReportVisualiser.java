package com.mmxlabs.lingo.reports.views.vertical;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.lingo.reports.ColourPalette;
import com.mmxlabs.models.lng.cargo.Cargo;
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

	public DateFormat createDateFormat() {
		return new SimpleDateFormat("dd/MMM/yy");
	}

	public Color getColorFor(final Date date, final SlotVisit visit) {
		final SlotAllocation allocation = visit.getSlotAllocation();
		final boolean isWindow = VerticalReportUtils.isDayOutsideActualVisit(date, visit);

		if (allocation != null) {
			final Slot slot = allocation.getSlot();
			if (slot != null) {
				final Cargo cargo = slot.getCargo();
				if (cargo != null && cargo.isAllowRewiring() == false) {
					return isWindow ? getColour(Light_Grey) : getColour(Grey);
				}
			}
		}
		return isWindow ? getColour(Light_Orange) : getSlotColour(visit);
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

	public Color getEventBackgroundColor(final Date date, final Event[] events) {

		final Event event = getRelevantEvent(date, events, PrecedenceType.COLOUR);

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

	public Font getEventFont(final Date element, final Event[] events) {
		return null;
	}

	abstract public Color getEventForegroundColor(Date element, Event[] events);

	public int getEventPrecedence(final Date date, final Event event, final PrecedenceType type) {
		if (type == PrecedenceType.COLOUR) {
			if (event instanceof SlotVisit) {
				return 5;
			}
			if (event instanceof Journey) {
				return -5;
			}
		} else if (type == PrecedenceType.TEXT) {
			if (event instanceof SlotVisit) {
				return VerticalReportUtils.isDayOutsideActualVisit(date, (SlotVisit) event) ? -10 : 5;
			}
			if (event instanceof Journey) {
				return -5;
			}

		}
		return 0;
	}

	public String getEventText(final Date date, final Event event) {
		if (date == null || event == null) {
			return "";
		}

		// how many days since the start of the event?
		Long days = (date.getTime() - event.getStart().getTime()) / (24 * 1000 * 3600);

		// Journey events just show the day number
		if (event instanceof Journey) {
			days += 1;
			return days.toString() + (days == 1 ? String.format(" (%.02f)", ((Journey) event).getSpeed()) : "");
		}

		else if (event instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) event;
			if (date != null && VerticalReportUtils.isDayOutsideActualVisit(date, visit)) {
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
		return eventClass.getName() + " '" + event.name() + "' " + days.toString();
	}

	public Color getSlotColour(final SlotVisit visit) {
		return getColour(Orange);
	}

	public Event getRelevantEvent(final Date date, final Event[] events, final PrecedenceType type) {
		Integer best = null;
		Event result = null;

		for (final Event event : events) {
			final int precedence = getEventPrecedence(date, event, type);
			if (result == null || best == null || precedence > best) {
				result = event;
				best = precedence;
			}
		}

		return result;
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
