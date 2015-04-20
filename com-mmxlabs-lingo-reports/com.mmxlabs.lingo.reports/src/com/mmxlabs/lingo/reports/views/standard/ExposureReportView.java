/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.joda.time.YearMonth;

import com.mmxlabs.lingo.reports.components.SimpleContentAndColumnProvider;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.Schedule;

/**
 */
public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> {
	public final Map<String, Map<YearMonth, Double>> overallExposures = new HashMap<String, Map<YearMonth, Double>>();

	@Override
	protected SimpleContentAndColumnProvider<IndexExposureData> createContentProvider() {
		// TODO Auto-generated method stub
		return new SimpleContentAndColumnProvider<IndexExposureData>() {

			/**
			 * Returns the list of year / month labels for the entire known exposure data range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			private List<YearMonth> dateRange() {
				List<YearMonth> result = new ArrayList<>();
				YearMonth earliest = null;
				YearMonth latest = null;

				for (Map<YearMonth, Double> exposures : overallExposures.values()) {
					for (YearMonth key : exposures.keySet()) {
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
			public List<ColumnManager<IndexExposureData>> getColumnManagers() {
				ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();

				result.add(new ColumnManager<IndexExposureData>("Index") {
					@Override
					public String getColumnText(IndexExposureData data) {
						return data.indexName;
					}

					@Override
					public int compare(IndexExposureData o1, IndexExposureData o2) {
						return o1.indexName.compareTo(o2.indexName);
					}
				});

				for (final YearMonth date : dateRange()) {
					result.add(new ColumnManager<IndexExposureData>(String.format("%04d-%02d", date.getYear(), date.getMonthOfYear())) {
						@Override
						public String getColumnText(IndexExposureData data) {
							double result = data.exposures.containsKey(date) ? data.exposures.get(date) : 0;
							return String.format("%,.01f", result);
						}

						@Override
						public int compare(IndexExposureData o1, IndexExposureData o2) {
							double result1 = o1.exposures.containsKey(date) ? o1.exposures.get(date) : 0;
							double result2 = o2.exposures.containsKey(date) ? o2.exposures.get(date) : 0;
							return Double.compare(result1, result2);
						}
					});
				}

				return result;
			}

			@Override
			protected List<IndexExposureData> createData(Schedule schedule, LNGScenarioModel rootObject, LNGPortfolioModel portfolioModel) {
				final List<IndexExposureData> output = new ArrayList<IndexExposureData>();

				PricingModel pm = rootObject.getPricingModel();
				EList<CommodityIndex> indices = pm.getCommodityIndices();

				overallExposures.clear();

				for (CommodityIndex index : indices) {
					Map<YearMonth, Double> exposures = Exposures.getExposuresByMonth(schedule, index);
					overallExposures.put(index.getName(), exposures);
					output.add(new IndexExposureData(index.getName(), exposures));
				}

				return output;
			}

		};
	}
}