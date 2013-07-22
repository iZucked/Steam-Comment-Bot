/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl;
import com.mmxlabs.models.lng.commercial.impl.SalesContractImpl;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * Class to display a report describing the total volume of gas transactions for each contract (purchase & sales) 
 * for each gas year (with additional rows for "other purchase" and "other sales"). 
 * 
 * @author Simon McGregor
 * @since 2.0
 */
public class VolumeTrackingReportView extends SimpleTabularReportView<VolumeTrackingReportView.VolumeData> {
	@SuppressWarnings("serial")
	public final Map<Contract, CumulativeMap<Integer>> overallVolumes = new AutoInitialisingMap<Contract, CumulativeMap<Integer>>() {
		@Override
		protected CumulativeMap<Integer> autoValue() {
			return new CumulativeMap<Integer>();
		}
	};
	
	private static PurchaseContract otherPurchase = new PurchaseContractImpl() { { name = "Other Purchase"; } };
	private static SalesContract otherSales = new SalesContractImpl() { { name = "Other Sales"; } };
	
	public static class VolumeData {
		public final Contract contract;
		public final Map<Integer, Integer> volumes;
		public VolumeData(Contract contract, Map<Integer, Integer> volumes) {
			this.contract = contract;
			this.volumes = volumes;
		}		
	}
	
	@SuppressWarnings("serial")
	private static abstract class AutoInitialisingMap<K, V> extends HashMap<K, V> {
		@Override
		public V get(Object key) {
			if (containsKey(key) == false) {
				put((K) key, autoValue());
			}
			return super.get(key);
		}

		abstract protected V autoValue();		
	}
	
	@SuppressWarnings("serial")
	private static class CumulativeMap<K> extends AutoInitialisingMap<K, Integer> {
		@Override
		protected Integer autoValue() {
			return 0;
		}
		
		public void plusEquals(K key, Integer value) {
			put(key, get(key) + value);
		}
		
	}
	
	
	/**
	 * Returns the gas year for a particular calendar date. The gas year starts in October
	 * and is based on UTC.
	 * 
	 * @param calendar
	 * @return
	 */
	private int getGasYear(Calendar calendar) {
		final Calendar utc = (Calendar) calendar.clone();
		// convert the date into UTC
		final int utcOffset = calendar.getTimeZone().getOffset(calendar.getTime().getTime());
		utc.add(Calendar.MILLISECOND, -utcOffset);				
		
		// subtract one year from the reported year for dates prior to october
		int yearOffset = (utc.get(Calendar.MONTH) < Calendar.OCTOBER) ? -1 : 0;
		
		return utc.get(Calendar.YEAR) + yearOffset;
	}
	

	@Override
	protected SimpleContentAndColumnProvider<VolumeData> createContentProvider() {

		return new SimpleContentAndColumnProvider<VolumeData>() {
			
			@Override
			protected List<VolumeData> createData(Schedule schedule, LNGScenarioModel rootObject, LNGPortfolioModel portfolioModel) {
				final List<VolumeData> output = new ArrayList<VolumeData>();

				overallVolumes.clear();
				
				for (CargoAllocation ca: schedule.getCargoAllocations()) {
					for (SlotAllocation sa: ca.getSlotAllocations()) {
						final int volume = sa.getVolumeTransferred();
						Contract contract = sa.getContract();
						if (contract == null) {
							Slot slot = sa.getSlot();
							if (slot instanceof LoadSlot) {
								contract = otherPurchase;
							}
							else if (slot instanceof DischargeSlot) {
								contract = otherSales;
							}
						}
						final int year = getGasYear(sa.getLocalStart());
						overallVolumes.get(contract).plusEquals(year, volume);						
					}
				}
				
				for (Contract contract: overallVolumes.keySet()) {
					output.add(new VolumeData(contract, overallVolumes.get(contract)));
				}

				return output;
			}
			
			/**
			 * Returns the list of year / month labels for the entire known exposure data range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			private List<Integer> yearRange() {
				List<Integer> result = new ArrayList<Integer>();
				Integer earliest = null;
				Integer latest = null;

				for (Map<Integer, Integer> volumes : overallVolumes.values()) {
					for (Integer key : volumes.keySet()) {
						if (earliest == null || earliest > key) {
							earliest = key;
						}
						if (latest == null || latest < key) {
							latest = key;
						}
					}
				}

				if (earliest == null) {
					return result;
				}

				for (int year = earliest; year <= latest; year++) {
					result.add(year);
				}

				return result;
			}

			@Override
			public List<ColumnManager<VolumeData>> getColumnManagers() {
				ArrayList<ColumnManager<VolumeData>> result = new ArrayList<ColumnManager<VolumeData>>();

				result.add(new ColumnManager<VolumeData>("Contract") {
					@Override
					public String getColumnText(VolumeData data) {
						return data.contract.getName();
					}

					@Override
					public int compare(VolumeData o1, VolumeData o2) {
						return o1.contract.getName().compareTo(o2.contract.getName());
					}
				});

				for (final int year : yearRange()) {
					result.add(new ColumnManager<VolumeData>(String.format("%d", year)) {
						@Override
						public String getColumnText(VolumeData data) {
							long result = data.volumes.containsKey(year) ? data.volumes.get(year) : 0;
							return String.format("%d", result);
						}

						@Override
						public int compare(VolumeData o1, VolumeData o2) {
							double result1 = o1.volumes.containsKey(year) ? o1.volumes.get(year) : 0;
							double result2 = o2.volumes.containsKey(year) ? o2.volumes.get(year) : 0;
							return Double.compare(result1, result2);
						}
					});
				}

				return result;
			}



		};
	}
}