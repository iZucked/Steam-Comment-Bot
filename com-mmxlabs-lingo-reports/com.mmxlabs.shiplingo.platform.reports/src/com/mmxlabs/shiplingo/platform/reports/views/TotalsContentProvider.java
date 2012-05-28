/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.Fuel;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
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
	public static final String TYPE_TIME = "Days, Hours";

	public static class RowData {
		public RowData(final String scheduleName, final String component, final String type, final long fitness) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.type = type;
			this.fitness = fitness;
		}

		public final String scheduleName;
		public final String component;
		public final String type;
		public final long fitness;

	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private void createRowData(final Schedule schedule, String scheduleName, final List<RowData> output) {
		/**
		 * Stores the total fuel costs for each type of fuel - this may not be the detailed output we want, I don't know
		 */
		final Map<Fuel, Long> totalFuelCosts = new HashMap<Fuel, Long>();

		long distance = 0l;
		long totalCost = 0l;
		long canals = 0;
		long hire = 0;
		
		long lateness = 0;

		for (final Sequence seq : schedule.getSequences()) {
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
				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
						
						long late = visit.getStart().getTime() -  visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime().getTime();
						lateness += (late / 1000 / 60/ 60);
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						long late = evt.getStart().getTime() -  vev.getVesselEvent().getStartBy().getTime();
						lateness += (late / 1000 / 60/ 60);
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
			output.add(new RowData(scheduleName, entry.getKey().toString(), TYPE_COST, entry.getValue()));
		}

		output.add(new RowData(scheduleName, "Canal Fees", TYPE_COST, canals));
		output.add(new RowData(scheduleName, "Charter Fees", TYPE_COST, hire));
		output.add(new RowData(scheduleName, "Distance", TYPE_COST, distance));
		
		output.add(new RowData(scheduleName, "Lateness", TYPE_TIME, lateness));

		output.add(new RowData(scheduleName, TOTAL_COST, TYPE_COST, totalCost));

		// compute revenues

		// final Map<Entity, Long> totalRevenue = new HashMap<Entity, Long>();
		//
		// for (final BookedRevenue revenue : schedule.getRevenue()) {
		// if (revenue.getEntity() == null) {
		// continue;
		// }
		// if ((revenue.getEntity() instanceof GroupEntity) == false) {
		// continue;
		// }
		// final Long l = totalRevenue.get(revenue.getEntity());
		// totalRevenue.put(revenue.getEntity(), (l == null ? 0 : l.longValue()) + revenue.getValue());
		// }
		//
		// long totalTotalRevenue = 0;
		// for (final Map.Entry<Entity, Long> sum : totalRevenue.entrySet()) {
		// output.add(new RowData(scheduleName, sum.getKey().getName(), false, sum.getValue()));
		// totalTotalRevenue += sum.getValue();
		// }
		//
		// output.add(new RowData(scheduleName, "Total Profit", false, totalTotalRevenue));
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		rowData = new RowData[0];
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					createRowData((Schedule) o, synchOutput.getScenarioInstance(o).getName(), rowDataList);
				}
			}
			rowData = rowDataList.toArray(rowData);
		}

	}

	@Override
	public void dispose() {

	}
}
