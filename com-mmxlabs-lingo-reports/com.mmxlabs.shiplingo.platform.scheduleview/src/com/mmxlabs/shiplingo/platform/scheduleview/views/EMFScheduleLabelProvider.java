/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Highlight_;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.SCHEDULER_VIEW_COLOUR_SCHEME;
import static com.mmxlabs.shiplingo.platform.scheduleview.views.SchedulerViewConstants.Show_Canals;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMemento;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartColourProvider;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Cooldown;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scheduler.optimiser.TradingConstants;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements IGanttChartToolTipProvider, IGanttChartColourProvider {

	private final Map<String, IScheduleViewColourScheme> colourSchemesById = new HashMap<String, IScheduleViewColourScheme>();
	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<IScheduleViewColourScheme>();

	private List<IScheduleViewColourScheme> highlighters = new ArrayList<IScheduleViewColourScheme>();
	private Map<String, IScheduleViewColourScheme> highlightersById = new HashMap<String, IScheduleViewColourScheme>();

	private IScheduleViewColourScheme currentScheme = null;

	private List<IScheduleViewColourScheme> currentHighlighters = new ArrayList<IScheduleViewColourScheme>();

	private final GanttChartViewer viewer;

	private final IMemento memento;

	final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	final DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

	private boolean showCanals = false;

	public EMFScheduleLabelProvider(final GanttChartViewer viewer, IMemento memento) {
		this.viewer = viewer;
		this.memento = memento;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		String text = null;
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;

			String seqText = sequence.getName();

			// Add scenario instance name to field if multiple scenarios are selected
			final Object input = viewer.getInput();
			if (input instanceof IScenarioViewerSynchronizerOutput) {
				final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;

				final Collection<Object> collectedElements = output.getCollectedElements();
				if (collectedElements.size() > 1) {
					final ScenarioInstance instance = output.getScenarioInstance(sequence.eContainer());
					seqText += "\n" + instance.getName();
				}
			}
			text = seqText;
		} else if (element instanceof Journey) {
			Journey j = (Journey) element;
			if (j.getRoute().contains("canal")) {
				if (memento.getBoolean(Show_Canals)) {
					text = j.getRoute().replace("canal", "");
				}
			}
		}
		return text;
	}

	public void setScheme(final IScheduleViewColourScheme scheme) {
		if (currentScheme != null) {
			currentScheme.setViewer(null);
		}
		this.currentScheme = scheme;
		if (currentScheme != null) {
			currentScheme.setViewer(viewer);
			memento.putString(SCHEDULER_VIEW_COLOUR_SCHEME, scheme.getID());
		}
	}

	public void toggleHighlighter(final String id) {
		if (highlightersById.containsKey(id)) {
			toggleHighlighter(highlightersById.get(id));
		}
	}

	public void toggleHighlighter(final IScheduleViewColourScheme hi) {

		for (IScheduleViewColourScheme highlighter : currentHighlighters) {
			if (hi != null && hi == highlighter) {
				currentHighlighters.remove(highlighter);
				highlighter.setViewer(null);
				memento.getChild(Highlight_).putBoolean(hi.getID(), false);
				return;
			}
		}
		if (hi != null) {
			currentHighlighters.add(hi);
			hi.setViewer(viewer);
			memento.putBoolean(hi.getID(), true);
		}
	}

	public void setScheme(final String id) {
		if (colourSchemesById.containsKey(id)) {
			setScheme(colourSchemesById.get(id));
		} else {
			setScheme(colourSchemes.get(0));
		}
	}

	public void toggleShowCanals() {
		showCanals = !showCanals;
		memento.putBoolean(Show_Canals, showCanals);
	}

	public boolean showCanals() {
		return showCanals;
	}

	public void addHighlighter(String id, IScheduleViewColourScheme cs) {
		if (id != null && !id.isEmpty()) {
			highlightersById.put(id, cs);
		}

		highlighters.add(cs);
	}

	protected final IScheduleViewColourScheme getCurrentScheme() {
		return currentScheme;
	}

	protected List<IScheduleViewColourScheme> getColourSchemes() {
		return colourSchemes;
	}

	protected List<IScheduleViewColourScheme> getHighlighters() {
		return highlighters;
	}

	protected final boolean isActive(IScheduleViewColourScheme hi) {
		return currentHighlighters.contains(hi);
	}

	private String dateToString(final Date date) {
		return df.format(date) + " " + tf.format(date);
	}

	@Override
	public String getToolTipText(final Object element) {
		if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {

			// build base string - start, end and time for duration.
			final StringBuilder tt = new StringBuilder();
			final StringBuilder eventText = new StringBuilder();
			if (element instanceof Event) {

				final String name = ((Event) element).name();
				if (name != null && !name.isEmpty()) {
					tt.append("ID: " + name + "\n");
				}
			}
			final Event event = (Event) element;
			final String start = dateToString(event.getStart());
			final String end = dateToString(event.getEnd());
			tt.append("Start: " + start + "\n");
			tt.append("End: " + end + "\n");
			final int days = event.getDuration() / 24;
			final int hours = event.getDuration() % 24;
			final String durationTime = days + " day" + (days > 1 || days == 0 ? "s" : "") + ", " + hours + " hour" + (hours > 1 || hours == 0 ? "s" : "");

			// build event specific text
			if (element instanceof Journey) {
				eventText.append("Travel time: " + durationTime + " \n");
				final Journey journey = (Journey) element;
				eventText.append(" \n");
				// if (!journey.getRoute().equalsIgnoreCase("default")) {
				eventText.append(String.format("%.1f knots", journey.getSpeed()));
				for (FuelQuantity fq : journey.getFuels()) {
					eventText.append(String.format(" | %s", fq.getFuel().toString()));
				}
				if (journey.getRoute().contains("canal")) {
					eventText.append(" | " + journey.getRoute() + "\n");
				}	

			} else if (element instanceof SlotVisit) {
				eventText.append("Time in port: " + durationTime + " \n");
				eventText.append(" \n");
				final SlotVisit slotVisit = (SlotVisit) element;
				final Slot slot = ((SlotVisit) element).getSlotAllocation().getSlot();
				// eventText.append("Window Start: " + dateToString(slot.getWindowStartWithSlotOrPortTime()) + "\n");
				eventText.append("Window End: " + dateToString(slot.getWindowEndWithSlotOrPortTime()) + "\n");

				if (slotVisit.getStart().after(slot.getWindowEndWithSlotOrPortTime())) {
					// lateness
					final Calendar localStart = slotVisit.getLocalStart();
					final Calendar windowEndDate = getWindowEndDate(element);
					eventText.append("LATE by " + getLatenessString(localStart, windowEndDate) + "\n");
				}
			} else if (element instanceof VesselEventVisit) {
				eventText.append("Duration: " + durationTime + "\n");
				final VesselEventVisit vev = (VesselEventVisit) element;
				if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
					// lateness;
					final Calendar localStart = vev.getLocalStart();
					final Calendar windowEndDate = getWindowEndDate(element);
					eventText.append(" \n");
					eventText.append("LATE by  " + getLatenessString(localStart, windowEndDate) + "\n");
				}
			} else if (element instanceof Idle) {
				eventText.append("Idle time: " + durationTime);
			} else if (element instanceof FuelUsage) {
				final FuelUsage fuel = (FuelUsage) element;
				for (final FuelQuantity fq : fuel.getFuels()) {
					eventText.append(String.format("%s: %,d %s | $%,d\n", fq.getFuel().toString(), fq.getAmounts().get(0).getQuantity(), fq.getAmounts().get(0).getUnit().toString(), fq.getCost()));
				}
			}

			{
				Integer value = getPnL(element);
				if (value != null) {
					eventText.append("\nP&L: " + String.format("$%,d", value.intValue()));
				}
			}

			tt.append(eventText);
			return tt.toString();
		}
		return null;
	}

	@Override
	public String getToolTipTitle(final Object element) {

		String port = null;
		if (element instanceof Event) {
			final Port portObj = ((Event) element).getPort();
			if (portObj != null) {
				port = portObj.getName();
			}
		}
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			return port + " -- " + journey.getDestination().getName() + (journey.isLaden() ? " (Laden)" : " (Ballast)");
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			return "At " + port + (idle.isLaden() ? " (Laden" : " (Ballast") + " idle)";
		} else if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {
			return "At " + port + (element instanceof Cooldown ? " (Cooldown)" : ""); // + displayTypeName;
		} else if (element instanceof SlotVisit) {
			final SlotVisit sv = (SlotVisit) element;
			return "At " + port + (sv.getSlotAllocation().getSlot() instanceof LoadSlot ? " (Load)" : "(Discharge)"); // + displayTypeName;
		}

		return null;
	}

	@Override
	public Image getToolTipImage(final Object element) {
		return null;// Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
	}

	@Override
	public Color getForeground(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getForeground(element);
		}
		return null;
	}

	@Override
	public Color getBackground(final Object element) {

		Color c = null;
		for (IScheduleViewColourScheme highlighter : currentHighlighters) {
			if (c == null)
				c = highlighter.getBackground(element);
		}

		if (c == null && currentScheme != null) {

			c = currentScheme.getBackground(element);
			// if (viewer.getSelection().isEmpty() == false) {
			// final ISelection selection = viewer.getSelection();
			// if (selection instanceof IStructuredSelection) {
			// if (((IStructuredSelection) selection).toList().contains(
			// element)) {
			// // darken selection
			// final float scaf = 0.8f;
			// return ColorCache.getColor(
			// (int) (color.getRed() * scaf),
			// (int) (color.getGreen() * scaf),
			// (int) (color.getBlue() * scaf));
			// }
			// }
			// }
		}
		return c;
	}

	public void addColourScheme(final String id, final IScheduleViewColourScheme colourScheme) {
		if (id != null && !id.isEmpty()) {
			colourSchemesById.put(id, colourScheme);
		}

		colourSchemes.add(colourScheme);
		if ((currentScheme == null) && (colourSchemes.size() == 1)) {
			currentScheme = colourScheme;
		}
	}

	private Calendar getWindowEndDate(final Object object) {
		final Date date;
		if (object instanceof SlotVisit) {
			date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			date = ((VesselEventVisit) object).getVesselEvent().getStartBy();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(FleetPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else {
			return null;
		}
	}

	public String getLatenessString(final Calendar localStart, final Calendar windowEndDate) {
		long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

		// Strip milliseconds
		diff /= 1000;
		// Strip seconds;
		diff /= 60;
		// Strip
		diff /= 60;
		if (diff / 24 == 0) {
			return String.format("%2dh", diff % 24);
		} else {
			return String.format("%2dd, %2dh", diff / 24, diff % 24);
		}
	}

	@Override
	public int getAlpha(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getAlpha(element);
		}
		return 255;
	}

	@Override
	public Color getBorderColour(final Object element) {
		if (currentScheme != null) {
			return currentScheme.getBorderColour(element);
		}
		return null;
	}

	@Override
	public int getBorderWidth(final Object element) {

		if (currentScheme != null) {
			return currentScheme.getBorderWidth(element);
		}
		return 1;
	}

	private Integer getPnL(Object object) {
		ExtraDataContainer container = null;

		if (object instanceof CargoAllocation) {
			container = (CargoAllocation) object;
		}
		if (object instanceof SlotVisit) {
			final SlotVisit slotVisit = (SlotVisit) object;
			if (slotVisit.getSlotAllocation().getSlot() instanceof LoadSlot) {
				container = slotVisit.getSlotAllocation().getCargoAllocation();
			}
		}
		if (object instanceof VesselEventVisit) {
			container = (VesselEventVisit) object;
		}
		if (object instanceof StartEvent) {
			container = (StartEvent) object;
		}
		if (object instanceof GeneratedCharterOut) {
			container = (GeneratedCharterOut) object;
		}

		if (container != null) {
			final ExtraData dataWithKey = container.getDataWithKey(TradingConstants.ExtraData_GroupValue);
			if (dataWithKey != null) {
				final Integer v = dataWithKey.getValueAs(Integer.class);
				if (v != null) {
					return v;
				}
			}
		}

		return null;

	}
}
