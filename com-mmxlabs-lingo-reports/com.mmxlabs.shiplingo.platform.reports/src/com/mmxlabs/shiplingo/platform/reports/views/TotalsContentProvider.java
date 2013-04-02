/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class TotalsContentProvider implements IStructuredContentProvider {

	private static final String TOTAL_COST = "Total Cost";

	private static final String TYPE_COST = "Cost";
	public static final String TYPE_TIME = "Days, hours";

	public static class RowData {
		public RowData(final String scheduleName, final String component, final String type, final long fitness, final boolean minimise) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.type = type;
			this.fitness = fitness;
			this.minimise = minimise;
		}

		public final String scheduleName;
		public final String component;
		public final String type;
		public final long fitness;
		public final boolean minimise;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private void createRowData(final Schedule schedule, final String scheduleName, final List<RowData> output) {
		/**
		 * Stores the total fuel costs for each type of fuel - this may not be the detailed output we want, I don't know
		 */
		final Map<Fuel, Long> totalFuelCosts = new HashMap<Fuel, Long>();

		long distance = 0l;
		long totalCost = 0l;
		long canals = 0;
		long hire = 0;
		long portCost = 0;

		long lateness = 0;
		long capacityViolations = 0;

		for (final Sequence seq : schedule.getSequences()) {

			int vesselCapacity = Integer.MAX_VALUE;
			final Vessel vessel = seq.getVessel();
			if (vessel != null) {
				vesselCapacity = vessel.getVesselClass().getCapacity();
			} else {
				final VesselClass vesselClass = seq.getVesselClass();
				if (vesselClass != null) {
					vesselCapacity = vesselClass.getCapacity();
				}
			}

			for (final Event evt : seq.getEvents()) {
				hire += evt.getHireCost();
				totalCost += evt.getHireCost();
				if (evt instanceof FuelUsage) {
					final FuelUsage mix = (FuelUsage) evt;
					// add up fuel components from mixture
					for (final FuelQuantity fq : mix.getFuels()) {
						Long sumSoFar = totalFuelCosts.get(fq.getFuel());
						if (sumSoFar == null)
							sumSoFar = 0l;
						totalFuelCosts.put(fq.getFuel(), sumSoFar + fq.getCost());
						totalCost += fq.getCost();
					}
				}
				if (evt instanceof Journey) {
					final Journey journey = (Journey) evt;
					distance += journey.getDistance();
					canals += journey.getToll();
					totalCost += journey.getToll();
				}
				if (evt instanceof PortVisit) {
					final int cost = ((PortVisit) evt).getPortCost();
					portCost += cost;
					totalCost += cost;
				}

				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					SlotAllocation slotAllocation = visit.getSlotAllocation();
					if (visit.getStart().after(slotAllocation.getSlot().getWindowEndWithSlotOrPortTime())) {

						final long late = visit.getStart().getTime() - slotAllocation.getSlot().getWindowEndWithSlotOrPortTime().getTime();
						lateness += (late / 1000 / 60 / 60);
					}

					if (slotAllocation.getSlot() != null) {
						Slot slot = slotAllocation.getSlot();
						final int minQuantity = slot.getMinQuantity();
						final int maxQuantity = slot.getMaxQuantity();
						if (maxQuantity != 0 && maxQuantity < slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						} else if (minQuantity != 0 && minQuantity > slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						} else if (vesselCapacity < slotAllocation.getVolumeTransferred()) {
							capacityViolations++;
						}
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						final long late = evt.getStart().getTime() - vev.getVesselEvent().getStartBy().getTime();
						lateness += (late / 1000 / 60 / 60);
					}
				}
			}
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}

		for (final Entry<Fuel, Long> entry : totalFuelCosts.entrySet()) {
			output.add(new RowData(scheduleName, entry.getKey().toString(), TYPE_COST, entry.getValue(), true));
		}

		output.add(new RowData(scheduleName, "Canal Fees", TYPE_COST, canals, true));
		output.add(new RowData(scheduleName, "Charter Fees", TYPE_COST, hire, true));
		output.add(new RowData(scheduleName, "Distance", TYPE_COST, distance, true));

		output.add(new RowData(scheduleName, "Port Costs", TYPE_COST, portCost, true));
		output.add(new RowData(scheduleName, "Lateness", TYPE_TIME, lateness, true));
		output.add(new RowData(scheduleName, "Capacity", "Count", capacityViolations, true));

		output.add(new RowData(scheduleName, TOTAL_COST, TYPE_COST, totalCost, true));
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		pinnedData.clear();
		rowData = new RowData[0];
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					if (synchOutput.isPinned(o)) {
						createRowData((Schedule) o, synchOutput.getScenarioInstance(o).getName(), pinnedData);
						rowDataList.addAll(pinnedData);
					} else {
						createRowData((Schedule) o, synchOutput.getScenarioInstance(o).getName(), rowDataList);
					}
				}
			}
			rowData = rowDataList.toArray(rowData);
		}

	}

	private final List<RowData> pinnedData = new ArrayList<RowData>();

	public List<RowData> getPinnedScenarioData() {
		return pinnedData;
	}

	@Override
	public void dispose() {

	}
}
