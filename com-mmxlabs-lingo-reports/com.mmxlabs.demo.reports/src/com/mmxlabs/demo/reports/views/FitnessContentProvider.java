/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.reports.views;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFitness;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessContentProvider implements IStructuredContentProvider {

	public class RowData {
		RowData(String c, long f) {
			this.component = c;
			this.fitness = f;
		}

		public final String component;
		public final long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		
		return rowData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput,
			final Object newInput) {

		if (newInput instanceof Schedule) {
			final Schedule schedule = (Schedule) newInput;
			final List<RowData> rowDataList = new LinkedList<RowData>();
			long total=0l;
			for (final ScheduleFitness f : schedule.getFitness()) {
				rowDataList.add(new RowData(f.getName(), f.getValue()));
				if (!(f.getName().equals("iterations") || f.getName().equals("runtime")))
					total += f.getValue();
			}
			rowDataList.add(new RowData("Total", total));
			
			rowData = rowDataList.toArray(rowData);
			
		} else {
			rowData = new RowData[]{};
		}
	}

	@Override
	public void dispose() {

	}
}
