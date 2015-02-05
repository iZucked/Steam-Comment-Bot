/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessContentProvider implements IStructuredContentProvider {

	public static class RowData {
		/**
		 */
		public RowData(final String scenario, final String component, final Long raw, final Double weight, final Long fitness) {
			super();
			this.scenario = scenario;
			this.component = component;
			this.raw = raw;
			this.weight = weight;
			this.fitness = fitness;
		}

		public final String scenario;
		public final String component;
		/**
		 */
		public final Long raw;
		/**
		 */
		public final Double weight;
		public final Long fitness;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {

		return rowData;
	}

	private final List<RowData> pinnedData = new ArrayList<RowData>();

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

					final LNGScenarioModel lngScenarioModel = svso.getLNGScenarioModel(o);

					final LNGPortfolioModel portfolioModel = lngScenarioModel.getPortfolioModel();
					if (portfolioModel != null) {
						final OptimiserSettings settings = portfolioModel.getParameters();
						final Map<String, Double> weightsMap = new HashMap<String, Double>();
						if (settings != null) {
							for (final Objective objective : settings.getObjectives()) {
								weightsMap.put(objective.getName(), objective.getWeight());
							}
						}

						final boolean isPinned = svso.isPinned(o);
						final List<RowData> destination = isPinned ? pinnedData : rowDataList;

						final Schedule schedule = (Schedule) o;

						long total = 0l;
						for (final Fitness f : schedule.getFitnesses()) {
							final Double weightObj = weightsMap.get(f.getName());
							final double weight = weightObj == null ? 0.0 : weightObj.doubleValue();
							final long raw = f.getFitnessValue();
							final long fitness = (long) (weight * (double) raw);
							destination.add(new RowData(svso.getScenarioInstance(o).getName(), f.getName(), raw, weight, fitness));
							if (!(f.getName().equals("iterations") || f.getName().equals("runtime"))) {
								total += fitness;
							}
						}
						destination.add(new RowData(svso.getScenarioInstance(o).getName(), "Total", null, null, total));

						if (isPinned) {
							rowDataList.addAll(pinnedData);
						}
					}
				}
			}
			rowData = rowDataList.toArray(rowData);
		}
	}

	@Override
	public void dispose() {

	}
}
