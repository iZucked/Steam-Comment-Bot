/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import scenario.Scenario;
import scenario.fleet.VesselState;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;

import com.mmxlabs.ganttviewer.GanttChartViewer;
import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
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

			final Scenario s = (Scenario) sequence.eContainer().eContainer().eContainer();
			final String name = s.getName();
			// final String name =
			// URI.decode(sequence.eResource().getURI().lastSegment()).replaceAll(".scenario","");

			return sequence.getVessel().getName() + "\n" + name;
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
		} else if (element instanceof ScheduledEvent) {

			final DateFormat df = DateFormat.getDateInstance();

			final StringBuilder sb = new StringBuilder();
			final ScheduledEvent event = (ScheduledEvent) element;

			sb.append("Start Time: " + df.format(event.getStartTime()) + "\n");
			sb.append("End Time: " + df.format(event.getEndTime()) + "\n");
			final int days = event.getEventDuration() / 24;
			final int hours = event.getEventDuration() % 24;
			sb.append("Duration: " + days + " days, " + hours + " hours\n");

			if (element instanceof PortVisit) {
				final PortVisit visit = (PortVisit) element;

				if (visit.getPort() != null) {
					sb.append("Port: " + visit.getPort().getName() + "\n");
				}
				if (element instanceof SlotVisit) {
					final SlotVisit svisit = (SlotVisit) element;
					sb.append("Window Start Time: " + svisit.getSlot().getWindowStart() + "\n");
					sb.append("Window End Time: " + svisit.getSlot().getWindowEnd() + "\n");
				} else if (element instanceof Idle) {
					final Idle idle = (Idle) element;
					sb.append("Vessel State: " + idle.getVesselState().getName() + "\n");
				}
			} else if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				if (journey.getFromPort() != null) {
					sb.append("From: " + journey.getFromPort().getName() + "\n");
				}
				if (journey.getToPort() != null) {
					sb.append("To: " + journey.getToPort().getName() + "\n");
				}
				// sb.append("Vessel State: " + journey.getVesselState().getName() + "\n");
				if (!journey.getRoute().equalsIgnoreCase("default")) {
					sb.append("Route: " + journey.getRoute() + "\n");
				}
				sb.append(String.format("Speed: %.1f\n", journey.getSpeed()));
				// sb.append("Distance: " + journey.getDistance() + "\n");
			}

			if (element instanceof FuelMixture) {
				final FuelMixture fuels = (FuelMixture) element;
				for (final FuelQuantity fq : fuels.getFuelUsage()) {
					if (fq.getQuantity() != 0) {
						sb.append(fq.getFuelType().getName() + ": ");
						// sb.append(fq.getQuantity() + " " + fq.getFuelUnit().getName());
						// sb.append(String.format(", cost $%,.0f\n", (double) fq.getTotalPrice()));
					}
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
			if (journey.getVesselState() == VesselState.LADEN) {
				return "Laden Journey";
			} else if (journey.getVesselState() == VesselState.BALLAST) {
				return "Ballast Journey";
			} else {
				return "Journey";
			}
		} else if (element instanceof Idle) {
			final Idle idle = (Idle) element;
			if (idle.getVesselState() == VesselState.LADEN) {
				return "Laden Idle";
			} else if (idle.getVesselState() == VesselState.BALLAST) {
				return "Ballast Idle";
			} else {
				return "Idle";
			}
		} else if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof PortVisit) {
			final String displayTypeName = ((PortVisit) element).getDisplayTypeName();
			return "Port Visit: " + displayTypeName;
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
