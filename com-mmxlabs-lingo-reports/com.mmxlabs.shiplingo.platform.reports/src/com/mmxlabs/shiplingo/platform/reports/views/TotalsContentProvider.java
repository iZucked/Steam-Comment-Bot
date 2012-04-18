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
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class TotalsContentProvider implements IStructuredContentProvider {

	public static class RowData {
		public RowData(final String scheduleName, final String component, final boolean isCost, final long fitness) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.isCost = isCost;
			this.fitness = fitness;
		}

		public final String scheduleName;
		public final String component;
		public final boolean isCost;
		public final long fitness;

	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private void createRowData(final Schedule schedule, final List<RowData> output) {
		/**
		 * Stores the total fuel costs for each type of fuel - this may not be the detailed output we want, I don't know
		 */
		final Map<Fuel, Long> totalFuelCosts = new HashMap<Fuel, Long>();

		long distance = 0l;
		long totalCost = 0l;
		long canals = 0;
		long hire = 0;

		for (final Sequence seq : schedule.getSequences()) {
			for (final Event evt : seq.getEvents()) {
				hire += evt.getHireCost();
				totalCost += evt.getHireCost();
				if (evt instanceof FuelUsage) {
					final FuelUsage mix = (FuelUsage) evt;
					// add up fuel components from mixture
					for (final FuelQuantity fq : mix.getFuels()) {
						Long sumSoFar = totalFuelCosts.get(fq.getFuel());
						if (sumSoFar == null) sumSoFar = 0l;
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
			}
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}
		final String scheduleName;
		if (object instanceof MMXRootObject) {
			final MMXRootObject s = (MMXRootObject) object;
			scheduleName = s.getName();
		} else {
			scheduleName = "";
		}
		for (final Entry<Fuel, Long> entry : totalFuelCosts.entrySet()) {
			output.add(new RowData(scheduleName, entry.getKey().toString(), true, entry.getValue()));
		}

		output.add(new RowData(scheduleName, "Canal Fees", true, canals));
		output.add(new RowData(scheduleName, "Charter Fees", true, hire));
		output.add(new RowData(scheduleName, "Distance", true, distance));

		output.add(new RowData(scheduleName, "Total Cost", true, totalCost));

		// compute revenues

//		final Map<Entity, Long> totalRevenue = new HashMap<Entity, Long>();
//
//		for (final BookedRevenue revenue : schedule.getRevenue()) {
//			if (revenue.getEntity() == null) {
//				continue;
//			}
//			if ((revenue.getEntity() instanceof GroupEntity) == false) {
//				continue;
//			}
//			final Long l = totalRevenue.get(revenue.getEntity());
//			totalRevenue.put(revenue.getEntity(), (l == null ? 0 : l.longValue()) + revenue.getValue());
//		}
//
//		long totalTotalRevenue = 0;
//		for (final Map.Entry<Entity, Long> sum : totalRevenue.entrySet()) {
//			output.add(new RowData(scheduleName, sum.getKey().getName(), false, sum.getValue()));
//			totalTotalRevenue += sum.getValue();
//		}
//
//		output.add(new RowData(scheduleName, "Total Profit", false, totalTotalRevenue));
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		rowData = new RowData[0];
		if (newInput instanceof Iterable) {
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : ((Iterable<Object>) newInput)) {
				if (o instanceof Schedule) {
					// construct data for schedule
					createRowData((Schedule) o, rowDataList);
				}
			}
			rowData = rowDataList.toArray(rowData);
		}
	}

	@Override
	public void dispose() {

	}
}
