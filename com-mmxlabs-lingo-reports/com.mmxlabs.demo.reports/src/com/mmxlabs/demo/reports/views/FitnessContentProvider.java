/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.demo.reports.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.Scenario;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFitness;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessContentProvider implements IStructuredContentProvider {

	public static class RowData {
		public RowData(final String scenario, final String component, final long fitness) {
			super();
			this.scenario = scenario;
			this.component = component;
			this.fitness = fitness;
		}

		public final String scenario;
		public final String component;
		public final long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {

		return rowData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		rowData = new RowData[0];
		if (newInput instanceof Iterable) {
			final List<RowData> rowDataList = new LinkedList<RowData>();
			for (final Object o : ((Iterable<?>) newInput)) {
				if (o instanceof Schedule) {
					final Schedule schedule = (Schedule) o;
					final Scenario s = (Scenario) schedule.eContainer().eContainer();
					final String scheduleName = s.getName();
					long total = 0l;
					for (final ScheduleFitness f : schedule.getFitness()) {
						rowDataList.add(new RowData(scheduleName, f.getName(), f.getValue()));
						if (!(f.getName().equals("iterations") || f.getName().equals("runtime"))) {
							total += f.getValue();
						}
					}
					rowDataList.add(new RowData(scheduleName, "Total", total));

				}
			}
			rowData = rowDataList.toArray(rowData);
		}
	}

	@Override
	public void dispose() {

	}
}
