/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

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

	private List<RowData> pinnedData = new ArrayList<RowData>();
	
	public List<RowData> getPinnedData() {
		return pinnedData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		rowData = new RowData[0];
		pinnedData.clear();
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput svso = (IScenarioViewerSynchronizerOutput) newInput;
			final List<RowData> rowDataList = new LinkedList<RowData>();
			for (final Object o : svso.getCollectedElements()) {
				if (o instanceof Schedule) {
					final List<RowData> destination = svso.isPinned(o) ? pinnedData : rowDataList;
					
					final Schedule schedule = (Schedule) o;
					
					long total = 0l;
					for (final Fitness f : schedule.getFitnesses()) {
						destination.add(new RowData(svso.getScenarioInstance(o).getName(), f.getName(), f.getFitnessValue()));
						if (!(f.getName().equals("iterations") || f.getName().equals("runtime"))) {
							total += f.getFitnessValue();
						}
					}
					destination.add(new RowData(svso.getScenarioInstance(o).getName(), "Total", total));

				}
			}
			rowData = rowDataList.toArray(rowData);
		}
	}

	@Override
	public void dispose() {

	}
}
