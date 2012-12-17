/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * @since 2.0
 */
public class ExposureReportView extends SimpleTabularReportView<IndexExposureData> {
	public final Map<String,Map<Date,Double>> overallExposures = new HashMap<String,Map<Date,Double>>(); 
	
	@Override
	protected SimpleContentAndColumnProvider<IndexExposureData> createContentProvider() {
		// TODO Auto-generated method stub
		return new SimpleContentAndColumnProvider<IndexExposureData> () {

			/**
			 * Returns the list of year / month labels for the entire known exposure data range.
			 * This may conceivably include months in which no transactions occur.
			 * @return
			 */
			private List<Date> dateRange() {
				List<Date> result = new ArrayList<Date>();
				Date earliest = null;
				Date latest = null;
				
				for (Map<Date, Double> exposures: overallExposures.values()) {
					for (Date key: exposures.keySet()) {
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
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(earliest);

				result.add(calendar.getTime());
				while (calendar.getTime().before(latest)) {
					calendar.add(Calendar.MONTH, 1);
					result.add(calendar.getTime());					
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
				
				for (final Date date: dateRange()) {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					result.add(new ColumnManager<IndexExposureData>(String.format("%d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1)) {
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
					Map<Date, Double> exposures = Exposures.getExposuresByMonth(schedule, index);
					overallExposures.put(index.getName(), exposures);
					output.add(new IndexExposureData(index.getName(), exposures));
				}
				
				return output;
			}
			
		};
	}
}