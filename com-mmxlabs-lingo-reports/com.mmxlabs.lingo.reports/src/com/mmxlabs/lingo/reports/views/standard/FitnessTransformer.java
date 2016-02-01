/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class FitnessTransformer {

	public static class RowData {
		/**
		 */
		public RowData(final String scenario, final String component, final Long raw, final Double weight, final Long fitness, final Long deltaFitness) {
			super();
			this.scenario = scenario;
			this.component = component;
			this.raw = raw;
			this.weight = weight;
			this.fitness = fitness;
			this.deltaFitness = deltaFitness;
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
		public final Long deltaFitness;
	}

	@NonNull
	public List<RowData> transform(@NonNull Schedule schedule, @NonNull final ISelectedDataProvider selectedDataProvider, @Nullable final List<RowData> pinnedData) {
		final List<RowData> rowDataList = new LinkedList<>();

		final LNGScenarioModel lngScenarioModel = selectedDataProvider.getScenarioModel(schedule);
		if (lngScenarioModel != null) {

			final OptimiserSettings settings = lngScenarioModel.getParameters();
			final Map<String, Double> weightsMap = new HashMap<String, Double>();
			if (settings != null) {
				for (final Objective objective : settings.getObjectives()) {
					weightsMap.put(objective.getName(), objective.getWeight());
				}
			}

			long total = 0l;
			for (final Fitness f : schedule.getFitnesses()) {
				final Double weightObj = weightsMap.get(f.getName());
				final double weight = weightObj == null ? 0.0 : weightObj.doubleValue();
				final long raw = f.getFitnessValue();
				final long fitness = (long) (weight * (double) raw);
				final Long deltaFitness = pinnedData == null ? null : getDelta(f.getName(), fitness, pinnedData);
				rowDataList.add(createRow(selectedDataProvider.getScenarioInstance(schedule), f.getName(), weight, raw, fitness, deltaFitness));
				if (!(f.getName().equals("iterations") || f.getName().equals("runtime"))) {
					total += fitness;
				}
			}
			rowDataList.add(createRow(selectedDataProvider.getScenarioInstance(schedule), "Total", null, null, total, null));
		}
		return rowDataList;
	}

	private RowData createRow(ScenarioInstance scenarioInstance, final String f, final Double weight, final Long raw, final Long fitness, final Long deltaFitness) {
		String name = scenarioInstance == null ? "" : scenarioInstance.getName();
		return new RowData(name, f, raw, weight, fitness, deltaFitness);
	}

	private Long getDelta(@NonNull final String component, final long fitness, @Nullable final List<RowData> pinnedData) {
		if (pinnedData == null) {
			return null;
		}
		for (final RowData data : pinnedData) {
			if (component.equals(data.component)) {
				return data.fitness - fitness;
			}
		}
		return null;
	}
}
