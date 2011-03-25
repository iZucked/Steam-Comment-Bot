/**
 *
 * Copyright (C) Minimax Labs Ltd., 2010 
 * All rights reserved. 
 * 
 */
package com.mmxlabs.scheduleview.views;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import scenario.schedule.Sequence;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;
import scenario.schedule.events.Journey;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;
import scenario.schedule.events.SlotVisit;

import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;

/**
 * @author hinton
 * 
 */
public class EMFScheduleLabelProvider extends BaseLabelProvider implements
		IGanttChartToolTipProvider, IColorProvider {
	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {
		if (element instanceof Sequence) {
			final Sequence sequence = (Sequence) element;
			return sequence.getVessel().getName() + "\n" + sequence.eResource().getURI().lastSegment();
		}
		return null;
	}

	public enum Mode {
		VesselState, FuelChoice, Canal
		/* , Lateness */
	}

	private Mode mode = Mode.VesselState;

	public void setMode(Mode mode) {
		this.mode = mode;
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
			sb.append("Duration: " + event.getDuration() + " hours\n");

			if (element instanceof SlotVisit) {
				final SlotVisit visit = (SlotVisit) element;
				
				sb.append("Port: "
						+ visit.getPort().getName() + "\n");
				sb.append("Window Start Time: "
						+ visit.getSlot().getWindowStart() + "\n");
				sb.append("Window End Time: "
						+ visit.getSlot().getWindowEnd() + "\n");
			} else if (element instanceof Idle) {
				final Idle idle = (Idle) element;
				
				sb.append("Port: "
						+ idle.getPort().getName() + "\n");
				sb.append("Vessel State: " + idle.getVesselState().getName() + "\n");
			} else if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				sb.append("From: "
						+ journey.getFromPort().getName() + "\n");
				sb.append("To: " + journey.getToPort().getName()
						+ "\n");
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
								(double)fq.getTotalPrice())); 
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
		return null;
	}

	@Override
	public Color getBackground(final Object element) {
		switch (mode) {
		case VesselState:
			if (element instanceof Journey) {
				final Journey journey = (Journey) element;
				if (journey.getVesselState().equals(scenario.fleet.VesselState.LADEN)) {
					return ColorCache.getColor(0, 255, 0);
				} else {					
					return ColorCache.getColor(0, 0, 255);
				}
			}
			break;
		case FuelChoice:
			if (element instanceof Journey) {
				final Journey journey = (Journey) element;

				int r = 0;
				int g = 0;
				int b = 0;

				for (final FuelQuantity fq : journey.getFuelUsage()) {
					if (fq.getQuantity() > 0) {
						switch (fq.getFuelType()) {
						case BASE_FUEL:
							r = 255;
							break;
						case FBO:
							b = 255;
							break;
						case NBO:
							g = 255;
							break;
						}
					}
				}
				
				return ColorCache.getColor(r, g, b);
			}
			break;
		case Canal:
			if (element instanceof Journey) {
				if (((Journey) element).getRouteCost() > 0) {
					return ColorCache.getColor(0,0,255);
				}
			}
			break;
		}

		// else if (mode == Mode.Lateness) {
		if (element instanceof SlotVisit) {
			final SlotVisit visit = (SlotVisit) element;
			
			if (visit.getSlot().getWindowEnd().before(visit.getStartTime())) {
				return ColorCache.getColor(255,0,0);
			}
//			
//			if (event.getPortSlot().getTimeWindow() != null
//					&& event.getPortSlot().getTimeWindow().getEnd() < event
//							.getStartTime()) {
//				return ColorCache.getColor(255, 0, 0);
//			} else if (event.getPortSlot() instanceof ICharterOutPortSlot) {
//				return ColorCache.getColor(100, 100, 100);
//			}
		}
		return null;
	}

}
