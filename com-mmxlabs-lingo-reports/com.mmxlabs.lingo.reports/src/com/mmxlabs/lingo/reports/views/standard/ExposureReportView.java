/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 */
public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> {
	public final Map<String, Map<YearMonth, Double>> overallExposures = new HashMap<String, Map<YearMonth, Double>>();

	public ExposureReportView() {
		super("com.mmxlabs.lingo.doc.Reports_IndexExposures");
	}

	@Override
	protected AbstractSimpleTabularReportContentProvider<IndexExposureData> createContentProvider() {
		return new AbstractSimpleTabularReportContentProvider<IndexExposureData>() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}
		};
	}

	@Override
	protected AbstractSimpleTabularReportTransformer<IndexExposureData> createTransformer() {
		return new AbstractSimpleTabularReportTransformer<IndexExposureData>() {

			/**
			 * Returns the list of year / month labels for the entire known exposure data range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			private List<YearMonth> dateRange() {
				final List<YearMonth> result = new ArrayList<>();
				YearMonth earliest = null;
				YearMonth latest = null;

				for (final Map<YearMonth, Double> exposures : overallExposures.values()) {
					for (final YearMonth key : exposures.keySet()) {
						if (earliest == null || earliest.isAfter(key)) {
							earliest = key;
						}
						if (latest == null || latest.isBefore(key)) {
							latest = key;
						}
					}
				}

				if (earliest == null) {
					return result;
				}

				YearMonth my = earliest;

				result.add(my);
				while (my.isBefore(latest)) {
					my = my.plusMonths(1);
					result.add(my);
				}
				return result;
			}

			@Override
			public List<ColumnManager<IndexExposureData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();

				if (selectedDataProvider.getScenarioInstances().size() > 1) {
					result.add(new ColumnManager<IndexExposureData>("Scenario") {

						@Override
						public String getColumnText(final IndexExposureData data) {
							final ScenarioInstance scenarioInstance = selectedDataProvider.getScenarioInstance(data.schedule);
							if (scenarioInstance != null) {
								return scenarioInstance.getName();
							}
							return null;
						}

						@Override
						public int compare(final IndexExposureData o1, final IndexExposureData o2) {
							final String s1 = getColumnText(o1);
							final String s2 = getColumnText(o2);
							if (s1 == null) {
								return -1;
							}
							if (s2 == null) {
								return 1;
							}
							return s1.compareTo(s2);
						}

						@Override
						public boolean isTree() {
							return false;
						}
					});
				}

				result.add(new ColumnManager<IndexExposureData>("Index") {
					@Override
					public String getColumnText(final IndexExposureData data) {
						return data.indexName;
					}

					@Override
					public int compare(final IndexExposureData o1, final IndexExposureData o2) {
						return o1.indexName.compareTo(o2.indexName);
					}
				});

				for (final YearMonth date : dateRange()) {
					result.add(new ColumnManager<IndexExposureData>(String.format("%04d-%02d", date.getYear(), date.getMonthValue())) {
						@Override
						public String getColumnText(final IndexExposureData data) {
							final double result = data.exposures.containsKey(date) ? data.exposures.get(date) : 0;
							return String.format("%,.01f", result);
						}

						@Override
						public int compare(final IndexExposureData o1, final IndexExposureData o2) {
							final double result1 = o1.exposures.containsKey(date) ? o1.exposures.get(date) : 0;
							final double result2 = o2.exposures.containsKey(date) ? o2.exposures.get(date) : 0;
							return Double.compare(result1, result2);
						}
					});
				}

				return result;
			}

			@Override
			public List<IndexExposureData> createData(final Schedule schedule, final LNGScenarioModel rootObject) {
				final List<IndexExposureData> output = new ArrayList<IndexExposureData>();

				final PricingModel pm = rootObject.getReferenceModel().getPricingModel();
				final EList<CommodityIndex> indices = pm.getCommodityIndices();

				overallExposures.clear();

				for (final CommodityIndex index : indices) {
					final Map<YearMonth, Double> exposures = Exposures.getExposuresByMonth(schedule, index, ScenarioModelUtil.getPricingModel(rootObject));
					overallExposures.put(index.getName(), exposures);
					output.add(new IndexExposureData(schedule, index.getName(), exposures));
				}

				return output;
			}

		};
	}
}