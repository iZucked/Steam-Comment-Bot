/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scheduleview.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import scenario.Scenario;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;

import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.scheduleview.views.colourschemes.IScheduleViewColourScheme;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements
		IGanttChartToolTipProvider, IColorProvider {

	private final List<IScheduleViewColourScheme> colourSchemes = new ArrayList<IScheduleViewColourScheme>();

	private IScheduleViewColourScheme currentScheme = null;

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;

			final Scenario s = (Scenario) sequence.eContainer().eContainer()
					.eContainer();
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
			final StringBuilder sb = new StringBuilder();
			final ScheduledEvent event = (ScheduledEvent) element;

			sb.append("Start Time: " + event.getStartTime() + "\n");
			sb.append("End Time: " + event.getEndTime() + "\n");
			sb.append("Duration: " + event.getEventDuration() + " hours\n");

			if (element instanceof PortVisit) {
				final PortVisit visit = (PortVisit) element;

				sb.append("Port: " + visit.getPort().getName() + "\n");
				if (element instanceof SlotVisit) {
					final SlotVisit svisit = (SlotVisit) element;
					sb.append("Window Start Time: "
							+ svisit.getSlot().getWindowStart() + "\n");
					sb.append("Window End Time: "
							+ svisit.getSlot().getWindowEnd() + "\n");
				} else if (element instanceof Idle) {
					final Idle idle = (Idle) element;
					sb.append("Vessel State: "
							+ idle.getVesselState().getName() + "\n");
				}
			} else if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				sb.append("From: " + journey.getFromPort().getName() + "\n");
				sb.append("To: " + journey.getToPort().getName() + "\n");
				sb.append("Vessel State: " + journey.getVesselState().getName()
						+ "\n");
				sb.append("Route: " + journey.getRoute() + "\n");
				sb.append("Speed: " + journey.getSpeed() + "\n");
				sb.append("Distance: " + journey.getDistance() + "\n");
			}

			if (element instanceof FuelMixture) {
				final FuelMixture fuels = (FuelMixture) element;
				for (final FuelQuantity fq : fuels.getFuelUsage()) {
					if (fq.getQuantity() != 0) {
						sb.append(fq.getFuelType().getName() + ": ");
						sb.append(fq.getQuantity() + " "
								+ fq.getFuelUnit().getName());
						sb.append(String.format(", cost $%,.2f\n",
								(double) fq.getTotalPrice()));
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
			return "Journey";
		} else if (element instanceof Idle) {
			return "Idle";
		} else if (element instanceof Sequence) {
			return getText(element);
		} else if (element instanceof PortVisit) {
			return "Port Visit";
		}
		return null;
	}

	@Override
	public Image getToolTipImage(final Object element) {
		return Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
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
			return currentScheme.getBackground(element);
		}
		return null;
	}

	public void addColourScheme(IScheduleViewColourScheme colourScheme) {
		colourSchemes.add(colourScheme);
		if (currentScheme == null && colourSchemes.size() == 1)
			currentScheme = colourScheme;
	}
}
