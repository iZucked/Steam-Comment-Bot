/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.shiplingo.platform.reports.Exposures;
import com.mmxlabs.shiplingo.platform.reports.Exposures.MonthYear;

/**
 * @since 2.0
 */
public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> {
	public final Map<String,Map<MonthYear,Double>> overallExposures = new HashMap<String,Map<MonthYear,Double>>(); 
	
	@Override
	protected SimpleContentAndColumnProvider<IndexExposureData> createContentProvider() {
		// TODO Auto-generated method stub
		return new SimpleContentAndColumnProvider<IndexExposureData> () {

			/**
			 * Returns the list of year / month labels for the entire known exposure data range.
			 * This may conceivably include months in which no transactions occur.
			 * @return
			 */
			private List<MonthYear> dateRange() {
				List<MonthYear> result = new ArrayList<MonthYear>();
				MonthYear earliest = null;
				MonthYear latest = null;
				
				for (Map<MonthYear, Double> exposures: overallExposures.values()) {
					for (MonthYear key: exposures.keySet()) {
						if (earliest == null || earliest.after(key)) {
							earliest = key;
						}
						if (latest == null || latest.before(key)) {
							latest = key;
						}
					}
				}
				
				if (earliest == null) {
					return result;
				}

				MonthYear my = earliest;

				result.add(my);
				while (my.before(latest)) {
					my = my.addMonths(1);
					result.add(my);
				}
				return result;
			}
			
			@Override
			public List<ColumnManager<IndexExposureData>> getColumnManagers() {
				ArrayList<ColumnManager<IndexExposureData>> result = new ArrayList<ColumnManager<IndexExposureData>>();
				
				result.add(new ColumnManager<IndexExposureData>("Index") {
					public String getColumnText(IndexExposureData data) {
						return data.indexName;
					}
					
					public int compare(IndexExposureData o1, IndexExposureData o2) {
						return o1.indexName.compareTo(o2.indexName);
					}			
				});
				
				for (final MonthYear date: dateRange()) {
					result.add(new ColumnManager<IndexExposureData>(String.format("%d-%02d", date.getYear(), date.getMonth())) {
						public String getColumnText(IndexExposureData data) {
							double result = data.exposures.containsKey(date) ? data.exposures.get(date) : 0; 
							return Double.toString(result);
						}
						
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
			protected List<IndexExposureData> createData(Schedule schedule,
					MMXRootObject root) {
				final List<IndexExposureData> output = new ArrayList<IndexExposureData>();
				
				PricingModel pm = root.getSubModel(PricingModel.class);
				EList<Index<Double>> indices = pm.getCommodityIndices();
				
				overallExposures.clear();
				
				for (Index<Double> index: indices) {
					Map<MonthYear, Double> exposures = Exposures.getExposuresByMonth(schedule, index);
					overallExposures.put(index.getName(), exposures);
					output.add(new IndexExposureData(index.getName(), exposures));
				}
				
				return output;
			}
			
		};
	}
}