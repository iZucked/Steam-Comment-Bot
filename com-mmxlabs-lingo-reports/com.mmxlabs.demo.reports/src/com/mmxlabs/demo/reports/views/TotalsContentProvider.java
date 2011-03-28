/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.EnumMap;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.schedule.Schedule;
import scenario.schedule.Sequence;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.FuelType;
import scenario.schedule.events.Journey;
import scenario.schedule.events.ScheduledEvent;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class TotalsContentProvider implements IStructuredContentProvider {

	public class RowData {
		RowData(String c, long f) {
			this.component = c;
			this.fitness = f;
		}

		public String component;
		public long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer,
			final Object oldInput, final Object newInput) {

		Schedule schedule = null;
		if (newInput instanceof Schedule) {
			schedule = (Schedule) newInput;
		}
		else if (newInput instanceof IAdaptable) {
			schedule = (Schedule) ((IAdaptable) newInput).getAdapter(Schedule.class);
		}
		
		if (schedule == null) {
			rowData = new RowData[0];
			return;
		}

		/**
		 * Stores the total fuel costs for each type of fuel - this may not be
		 * the detailed output we want, I don't know
		 */
		final EnumMap<FuelType, Long> totalFuelCosts = new EnumMap<FuelType, Long>(
				FuelType.class);

		for (final FuelType t : FuelType.values()) {
			totalFuelCosts.put(t, 0l);
		}

		long distance = 0l;
		long totalCost = 0l;
		long canals = 0;
		long hire = 0;

		for (final Sequence seq : schedule.getSequences()) {
			for (final ScheduledEvent evt : seq.getEvents()) {
				hire += evt.getHireCost();
				totalCost += evt.getHireCost();
				if (evt instanceof FuelMixture) {
					final FuelMixture mix = (FuelMixture) evt;
					// add up fuel components from mixture
					for (final FuelQuantity fq : mix.getFuelUsage()) {
						final long sumSoFar = totalFuelCosts.get(fq
								.getFuelType());
						totalFuelCosts.put(fq.getFuelType(),
								sumSoFar + fq.getTotalPrice());
						totalCost += fq.getTotalPrice();
					}
				}
				if (evt instanceof Journey) {
					final Journey journey = (Journey) evt;
					distance += journey.getDistance();
					canals += journey.getRouteCost();
					totalCost += journey.getRouteCost();
				}	
			}
		}

		rowData = new RowData[totalFuelCosts.size() + 4];

		int idx = 0;
		for (final Entry<FuelType, Long> entry : totalFuelCosts.entrySet()) {
			rowData[idx++] = new RowData(entry.getKey().toString(),
					entry.getValue());
		}
		rowData[idx++] = new RowData("Canal Fees", canals);
		rowData[idx++] = new RowData("Charter Fees", hire);
		rowData[idx++] = new RowData("Distance", distance);
		
		rowData[idx++] = new RowData("Total Cost", totalCost);
		
	}

	@Override
	public void dispose() {

	}
}
