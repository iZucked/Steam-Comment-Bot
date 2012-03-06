/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.validation.internal.modeled.model.validation.util.ValidationSwitch;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.ValidationSupport;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduleview.views.colourschemes.IScheduleViewColourScheme;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements IGanttChartToolTipProvider, IColorProvider {

	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<IScheduleViewColourScheme>();

	private IScheduleViewColourScheme currentScheme = null;

	private final GanttChartViewer viewer;

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

			final MMXRootObject root = ValidationSupport.getInstance().getParentObjectType(MMXRootObject.class, sequence);
			//final String name = root.getName();
			// final String name =
			// URI.decode(sequence.eResource().getURI().lastSegment()).replaceAll(".scenario","");

			return sequence.getName();
		}
		return null;
	}

	public void setScheme(final IScheduleViewColourScheme scheme) {
		this.currentScheme = scheme;
	}

	protected final IScheduleViewColourScheme getCurrentScheme() {
		return currentScheme;
	}

	protected List<IScheduleViewColourScheme> getColourSchemes() {
		return colourSchemes;
	}

	@Override
	public String getToolTipText(final Object element) {
		if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {

			final DateFormat df = DateFormat.getDateInstance();

			final StringBuilder sb = new StringBuilder();
			final Event event = (Event) element;

			sb.append("Start Time: " + df.format(event.getStart()) + "\n");
			sb.append("End Time: " + df.format(event.getEnd()) + "\n");
			final int days = event.getDuration() / 24;
			final int hours = event.getDuration() % 24;
			sb.append("Duration: " + days + " days, " + hours + " hours\n");

			if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				if (journey.getPort() != null) {
					sb.append("From: " + journey.getPort().getName() + "\n");
				}
				if (journey.getDestination() != null) {
					sb.append("To: " + journey.getDestination().getName() + "\n");
				}
				// sb.append("Vessel State: " + journey.getVesselState().getName() + "\n");
				if (!journey.getRoute().equalsIgnoreCase("default")) {
					sb.append("Route: " + journey.getRoute() + "\n");
				}
				sb.append(String.format("Speed: %.1f\n", journey.getSpeed()));
			} else {
				if (event.getPort() != null) {
					sb.append("Port: " + event.getPort().getName() + "\n");
				}
				if (element instanceof SlotVisit) {
					final SlotVisit svisit = (SlotVisit) element;
					sb.append("Window Start Time: " + svisit.getSlotAllocation().getSlot().getWindowStartWithSlotOrPortTime() + "\n");
					sb.append("Window End Time: " + svisit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime() + "\n");
				} else if (element instanceof Idle) {
					final Idle idle = (Idle) element;
					sb.append("Laden: " + (idle.isLaden() ? "Yes" : "No") + "\n");
				}
			}

			if (element instanceof FuelUsage) {
				final FuelUsage fuel = (FuelUsage) element;
				for (final FuelQuantity fq : fuel.getFuels()) {
					sb.append(String.format("%s, %,d %s, $%,d\n",
							fq.getFuel().toString(), fq.getAmounts().get(0).getQuantity(), fq.getAmounts().get(0).getUnit().toString(),
							fq.getCost()));
				}
			}

			return sb.toString();
		}
		return null;
	}

	@Override
	public String getToolTipTitle(final Object element) {
		if (element instanceof Journey) {
			final Journey journey = (Journey) element;
			return (journey.isLaden() ? "Laden" : "Ballast") + " Journey";
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			return (idle.isLaden() ? "Laden" : "Ballast") + " Idle";
		} else if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof Event) {
//			final String displayTypeName = ((PortVisit) element).getDisplayTypeName();
			return "Port Visit: ";// + displayTypeName;
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

	public void addColourScheme(final IScheduleViewColourScheme colourScheme) {
		colourSchemes.add(colourScheme);
		if ((currentScheme == null) && (colourSchemes.size() == 1)) {
			currentScheme = colourScheme;
		}
	}
}
