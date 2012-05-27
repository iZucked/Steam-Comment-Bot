/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.scheduleview.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements IGanttChartToolTipProvider, IColorProvider {

	private final Map<String, IScheduleViewColourScheme> colourSchemesById = new HashMap<String, IScheduleViewColourScheme>();

	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<IScheduleViewColourScheme>();

	private IScheduleViewColourScheme currentScheme = null;

	private final GanttChartViewer viewer;

	final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
	final DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

	public EMFScheduleLabelProvider(final GanttChartViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;

			String text = sequence.getName();

			// Add scenario instance name to field if multiple scenarios are selected
			final Object input = viewer.getInput();
			if (input instanceof IScenarioViewerSynchronizerOutput) {
				final IScenarioViewerSynchronizerOutput output = (IScenarioViewerSynchronizerOutput) input;

				final Collection<Object> collectedElements = output.getCollectedElements();
				if (collectedElements.size() > 1) {
					final ScenarioInstance instance = output.getScenarioInstance(sequence.eContainer());
					text += "\n" + instance.getName();
				}
			}

			return text;
		}
		return null;
	}

	public void setScheme(final IScheduleViewColourScheme scheme) {
		this.currentScheme = scheme;
	}

	public void setScheme(final String id) {
		if (colourSchemesById.containsKey(id)) {
			this.currentScheme = colourSchemesById.get(id);
		}
	}

	protected final IScheduleViewColourScheme getCurrentScheme() {
		return currentScheme;
	}

	protected List<IScheduleViewColourScheme> getColourSchemes() {
		return colourSchemes;
	}

	
	private String dateToString (Date date){
		return 	df.format(date) + " " + tf.format(date);
	}

	@Override
	public String getToolTipText(final Object element) {
		if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {

			final StringBuilder sb = new StringBuilder();
			final Event event = (Event) element;			
			final String start = dateToString(event.getStart()) ;
			final String end = dateToString(event.getEnd()) ;
			
//			final String start = df.format(event.getStart()) + " " + tf.format(event.getStart());
//			final String end = df.format(event.getEnd());

//			if (element instanceof Journey) {
//				final Journey journey = (Journey) element;
//				if (journey.getPort() != null) {
//					sb.append("From: " + journey.getPort().getName() + "\n");
//				}
//				if (journey.getDestination() != null) {
//					sb.append("To: " + journey.getDestination().getName() + "\n");
//				}
//			}
//
//			sb.append("\n");

//			if (element instanceof Journey) {
//				final Journey journey = (Journey) element;
//				if (journey.getPort() != null) {
//					sb.append("Depart: " + start + ", " + journey.getPort().getName() + "\n");
//				}
//				if (journey.getDestination() != null) {
//					sb.append("Arrive: " + end + ", " + journey.getDestination().getName() + "\n");
//				}
//			}
//
			
			sb.append("Start: " + start + "\n");
			sb.append("End:  " + end + "\n");
			final int days = event.getDuration() / 24;
			final int hours = event.getDuration() % 24;
			sb.append("Duration: " + days + " days, " + hours + " hours\n");
			if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				// sb.append("Vessel State: " + journey.getVesselState().getName() + "\n");
				if (!journey.getRoute().equalsIgnoreCase("default")) {
					sb.append("Route: " + journey.getRoute() + "\n");
				}
				sb.append(String.format("Speed: %.1f\n", journey.getSpeed()));
			} 
			else {
				if (event.getPort() != null) {
//					sb.append("Port: " + event.getPort().getName() + "\n");
				}
				if (element instanceof SlotVisit) {
					final Slot slot = ((SlotVisit) element).getSlotAllocation().getSlot();
					sb.append("Window Start: " + dateToString(slot.getWindowStartWithSlotOrPortTime()) + "\n");
					sb.append("Window End: " + dateToString(slot.getWindowEndWithSlotOrPortTime()) + "\n");
				} else if (element instanceof Idle) {
//					final Idle idle = (Idle) element;
//					sb.append("Laden: " + (idle.isLaden() ? "Yes" : "No") + "\n");
				}
			}

			if (element instanceof FuelUsage) {
				final FuelUsage fuel = (FuelUsage) element;
				for (final FuelQuantity fq : fuel.getFuels()) {
					sb.append(String.format("%s: %,d %s | $%,d\n", fq.getFuel().toString(), fq.getAmounts().get(0).getQuantity(), fq.getAmounts().get(0).getUnit().toString(), fq.getCost()));
				}
			}

			return sb.toString();
		}
		return null;
	}

	@Override
	public String getToolTipTitle(final Object element) {
		String port = null;
		if (element instanceof Event) {
			port = ((Event) element).getPort().getName();
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
			return "At " +  port;// + displayTypeName;
		} else if (element instanceof SlotVisit) {
			SlotVisit sv = (SlotVisit) element;
			return "At " +  port;// + displayTypeName;
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
		if (currentScheme != null) {

			final Color color = currentScheme.getBackground(element);
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
			return color;
		}
		return null;
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

}
