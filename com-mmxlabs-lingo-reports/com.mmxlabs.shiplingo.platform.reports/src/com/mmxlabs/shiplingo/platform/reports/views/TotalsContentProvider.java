/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.Scenario;
import scenario.contract.Entity;
import scenario.contract.GroupEntity;
import scenario.schedule.BookedRevenue;
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
		final EnumMap<FuelType, Long> totalFuelCosts = new EnumMap<FuelType, Long>(FuelType.class);

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
						final long sumSoFar = totalFuelCosts.get(fq.getFuelType());
						totalFuelCosts.put(fq.getFuelType(), sumSoFar + fq.getTotalPrice());
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

		final Scenario s = (Scenario) schedule.eContainer().eContainer();
		final String scheduleName = s.getName();

		for (final Entry<FuelType, Long> entry : totalFuelCosts.entrySet()) {
			output.add(new RowData(scheduleName, entry.getKey().toString(), true, entry.getValue()));
		}

		output.add(new RowData(scheduleName, "Canal Fees", true, canals));
		output.add(new RowData(scheduleName, "Charter Fees", true, hire));
		output.add(new RowData(scheduleName, "Distance", true, distance));

		output.add(new RowData(scheduleName, "Total Cost", true, totalCost));

		// compute revenues

		final Map<Entity, Long> totalRevenue = new HashMap<Entity, Long>();

		for (final BookedRevenue revenue : schedule.getRevenue()) {
			if (revenue.getEntity() == null) {
				continue;
			}
			if ((revenue.getEntity() instanceof GroupEntity) == false) {
				continue;
			}
			final Long l = totalRevenue.get(revenue.getEntity());
			totalRevenue.put(revenue.getEntity(), (l == null ? 0 : l.longValue()) + revenue.getValue());
		}

		long totalTotalRevenue = 0;
		for (final Map.Entry<Entity, Long> sum : totalRevenue.entrySet()) {
			output.add(new RowData(scheduleName, sum.getKey().getName(), false, sum.getValue()));
			totalTotalRevenue += sum.getValue();
		}

		output.add(new RowData(scheduleName, "Total Profit", false, totalTotalRevenue));
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
