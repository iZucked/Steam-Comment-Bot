package com.mmxlabs.scheduleview.views;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.nebula.widgets.ganttchart.ColorCache;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.mmxlabs.ganttviewer.IGanttChartToolTipProvider;
import com.mmxlabs.optimiser.IResource;
import com.mmxlabs.scheduler.optimiser.Calculator;
import com.mmxlabs.scheduler.optimiser.components.VesselState;
import com.mmxlabs.scheduler.optimiser.events.IIdleEvent;
import com.mmxlabs.scheduler.optimiser.events.IJourneyEvent;
import com.mmxlabs.scheduler.optimiser.events.IPortVisitEvent;
import com.mmxlabs.scheduler.optimiser.events.IScheduledEvent;
import com.mmxlabs.scheduler.optimiser.voyage.FuelComponent;

@SuppressWarnings("rawtypes")
public class AnnotatedSequenceLabelProvider extends BaseLabelProvider implements
		IGanttChartToolTipProvider, IColorProvider {

	public enum Mode {
		VesselState, FuelChoice /*, Lateness*/
	}

	private Mode mode = Mode.VesselState;

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	@Override
	public Image getImage(final Object element) {
		return null;
	}

	@Override
	public String getText(final Object element) {

		if (element instanceof IResource) {
			IResource r = (IResource) element;
			return r.getName();
		}

		return null;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public Color getForeground(final Object element) {

		return null;
	}

	@Override
	public Color getBackground(final Object element) {

		if (mode == Mode.VesselState) {

			if (element instanceof IJourneyEvent) {
				final IJourneyEvent event = (IJourneyEvent) element;
				if (event.getVesselState() == VesselState.Laden) {
					return ColorCache.getColor(0, 255, 0);
				} else {
					return ColorCache.getColor(0, 0, 255);
				}
			}
		} else if (mode == Mode.FuelChoice) {
			if (element instanceof IJourneyEvent) {
				final IJourneyEvent event = (IJourneyEvent) element;
				
				int r = 0;
				int g = 0;
				int b = 0;
				
				if (event.getFuelConsumption(FuelComponent.Base) >0 || event.getFuelConsumption(FuelComponent.Base_Supplemental) > 0) {
					r = 255;
				}
				if (event.getFuelConsumption(FuelComponent.NBO) > 0) {
					g = 255;
				}
				if (event.getFuelConsumption(FuelComponent.FBO) > 0) {
					b = 255;
				}
				return ColorCache.getColor(r,g,b);
			}
		}
//		else if (mode == Mode.Lateness) {
			if (element instanceof IPortVisitEvent) {
				final IPortVisitEvent event = (IPortVisitEvent) element;
				
				int end = event.getPortSlot().getTimeWindow().getEnd();
				if (event.getStartTime() > end) {
					return ColorCache.getColor(255, 0, 0);
				}
			}
//		}
		return null;
	}

	@Override
	public String getToolTipText(final Object element) {

		if (element instanceof IResource) {
			return ((IResource) element).getName();
		} else if (element instanceof IPortVisitEvent) {
			final IPortVisitEvent portVisit = (IPortVisitEvent) element;
			final StringBuilder sb = new StringBuilder();
			sb.append("Port: " + portVisit.getPortSlot().getPort().getName()
					+ "\n");
			sb.append("Start Time: " + portVisit.getStartTime() + "\n");
			sb.append("End Time: " + portVisit.getEndTime() + "\n");
			sb.append("Window Start Time: " + portVisit.getPortSlot().getTimeWindow().getStart() + "\n");
			sb.append("Window End Time: " + portVisit.getPortSlot().getTimeWindow().getEnd() + "\n");

			sb.append("Duration: " + portVisit.getDuration() + "\n");
			return sb.toString();
		} else if (element instanceof IJourneyEvent) {
			final IJourneyEvent journey = (IJourneyEvent) element;
			final StringBuilder sb = new StringBuilder();
			sb.append("From Port: " + journey.getFromPort().getName() + "\n");
			sb.append("To Port: " + journey.getToPort().getName() + "\n");
			sb.append("Start Time: " + journey.getStartTime() + "\n");
			sb.append("End Time: " + journey.getEndTime() + "\n");
			sb.append("Duration: " + journey.getDuration() + "\n");
			sb.append("Distance: " + journey.getDistance() + "\n");
			sb.append("Vessel State: " + journey.getVesselState() + "\n");
			sb.append("Speed: "
					+ String.format("%.2f", ((double) journey.getSpeed())
							/ (double) Calculator.ScaleFactor) + "\n");
			for (FuelComponent fuel : FuelComponent.values()) {
				long cost = journey.getFuelCost(fuel);
				if (cost != 0) {
					sb.append(fuel
							+ " Cost: "
							+ String.format("$%,.2f", (double) cost
									/ (double) Calculator.ScaleFactor) + "\n");
				}
			}
			return sb.toString();
		} else if (element instanceof IIdleEvent) {
			final IIdleEvent idle = (IIdleEvent) element;
			final StringBuilder sb = new StringBuilder();
			sb.append("Port: " + idle.getPort().getName() + "\n");
			sb.append("Start Time: " + idle.getStartTime() + "\n");
			sb.append("End Time: " + idle.getEndTime() + "\n");
			sb.append("Duration: " + idle.getDuration() + "\n");
			sb.append("Vessel State: " + idle.getVesselState() + "\n");
			for (FuelComponent fuel : FuelComponent.values()) {
				long cost = idle.getFuelCost(fuel);
				if (cost != 0) {
					sb.append(fuel
							+ " Cost: "
							+ String.format("$%,.2f", (double) cost
									/ (double) Calculator.ScaleFactor) + "\n");
				}
			}
			return sb.toString();
		}
		return null;
	}

	@Override
	public String getToolTipTitle(final Object element) {

		if (element instanceof IResource) {
			return ((IResource) element).getName();
		} else if (element instanceof IPortVisitEvent) {
			return "Port Visit";
		} else if (element instanceof IJourneyEvent) {
			return "Journey";
		} else if (element instanceof IIdleEvent) {
			return "Idle";
		} else if (element instanceof IScheduledEvent) {
			return ((IScheduledEvent) element).getName();
		}
		return null;
	}

	@Override
	public Image getToolTipImage(final Object element) {

		return Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
	}

}
