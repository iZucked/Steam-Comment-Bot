/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.common.Equality;
import com.mmxlabs.common.Pair;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportContentProvider;
import com.mmxlabs.lingo.reports.components.AbstractSimpleTabularReportTransformer;
import com.mmxlabs.lingo.reports.services.ISelectedDataProvider;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.commercial.impl.PurchaseContractImpl;
import com.mmxlabs.models.lng.commercial.impl.SalesContractImpl;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Class to display a report describing the total volume of gas transactions for each contract (purchase & sales) for each gas year (with additional rows for "other purchase" and "other sales").
 * 
 * @author Simon McGregor
 */
public class VolumeTrackingReportView extends SimpleTabularReportView<VolumeTrackingReportView.VolumeData> {

	public VolumeTrackingReportView() {
		super("com.mmxlabs.lingo.doc.Reports_VolumeTracking");
	}

	@SuppressWarnings("serial")
	public final Map<Contract, CumulativeMap<Integer>> overallVolumes = new AutoInitialisingMap<Contract, CumulativeMap<Integer>>() {
		@Override
		protected CumulativeMap<Integer> autoValue() {
			return new CumulativeMap<Integer>();
		}
	};

	private static PurchaseContract otherPurchase = new PurchaseContractImpl() {
		{
			name = "Other Purchase";
		}
	};
	private static SalesContract otherSales = new SalesContractImpl() {
		{
			name = "Other Sales";
		}
	};

	public static class VolumeData {
		public final Contract contract;
		public final Map<Integer, Long> volumes;
		public final Schedule schedule;

		public VolumeData(@NonNull final Schedule schedule, final Contract contract, final Map<Integer, Long> volumes) {
			this.schedule = schedule;
			this.contract = contract;
			this.volumes = volumes;
		}
	}

	@SuppressWarnings("serial")
	private static abstract class AutoInitialisingMap<K, V> extends HashMap<K, V> {
		@Override
		public V get(final Object key) {
			if (containsKey(key) == false) {
				put((K) key, autoValue());
			}
			return super.get(key);
		}

		abstract protected V autoValue();
	}

	@SuppressWarnings("serial")
	private static class CumulativeMap<K> extends AutoInitialisingMap<K, Long> {
		@Override
		protected Long autoValue() {
			return 0l;
		}

		public void plusEquals(final K key, final Long value) {
			put(key, get(key) + value);
		}

	}

	/**
	 * Returns the gas year for a particular calendar date. The gas year starts in October and is based on UTC.
	 * 
	 * @param calendar
	 * @return
	 */
	private int getGasYear(final ZonedDateTime calendar) {
		final LocalDate utc = calendar.toLocalDate();

		// subtract one year from the reported year for dates prior to october
		final int yearOffset = (utc.getMonthValue() < Calendar.OCTOBER) ? -1 : 0;

		return utc.getYear() + yearOffset;
	}

	@Override
	protected AbstractSimpleTabularReportContentProvider<VolumeData> createContentProvider() {

		return new AbstractSimpleTabularReportContentProvider<VolumeData>() {

			@Override
			public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

			}
		};

	}

	@Override
	protected AbstractSimpleTabularReportTransformer<VolumeData> createTransformer() {

		return new AbstractSimpleTabularReportTransformer<VolumeData>() {
			@Override
			public @NonNull List<@NonNull VolumeData> createData(@Nullable Pair<@NonNull Schedule, @NonNull LNGScenarioModel> pinnedPair,
					@NonNull List<@NonNull Pair<@NonNull Schedule, @NonNull LNGScenarioModel>> otherPairs) {
				overallVolumes.clear();

				final List<@NonNull VolumeData> output = new LinkedList<>();
				{
					if (pinnedPair != null) {
						output.addAll(createData(pinnedPair.getFirst(), pinnedPair.getSecond()));
					}
					for (Pair<@NonNull Schedule, @NonNull LNGScenarioModel> p : otherPairs) {
						output.addAll(createData(p.getFirst(), p.getSecond()));
					}
				}

				return output;
			}

			public List<VolumeData> createData(final Schedule schedule, final LNGScenarioModel rootObject) {
				final List<VolumeData> output = new ArrayList<VolumeData>();

				for (final CargoAllocation ca : schedule.getCargoAllocations()) {
					for (final SlotAllocation sa : ca.getSlotAllocations()) {
						final long volume = sa.getVolumeTransferred();
						Contract contract = sa.getContract();
						if (contract == null) {
							final Slot slot = sa.getSlot();
							if (slot instanceof LoadSlot) {
								contract = otherPurchase;
							} else if (slot instanceof DischargeSlot) {
								contract = otherSales;
							}
						}
						final int year = getGasYear(sa.getSlotVisit().getStart());
						overallVolumes.get(contract).plusEquals(year, volume);
					}
				}

				for (final Contract contract : overallVolumes.keySet()) {
					output.add(new VolumeData(schedule, contract, overallVolumes.get(contract)));
				}

				return output;
			}

			/**
			 * Returns the list of year / month labels for the entire known exposure data range. This may conceivably include months in which no transactions occur.
			 * 
			 * @return
			 */
			private List<Integer> yearRange() {
				final List<Integer> result = new ArrayList<Integer>();
				Integer earliest = null;
				Integer latest = null;

				for (final Map<Integer, Long> volumes : overallVolumes.values()) {
					for (final Integer key : volumes.keySet()) {
						if (earliest == null || earliest > key) {
							earliest = key;
						}
						if (latest == null || latest < key) {
							latest = key;
						}
					}
				}

				if (earliest == null || latest == null) {
					return result;
				}

				for (int year = earliest; year <= latest; year++) {
					result.add(year);
				}

				return result;
			}

			@Override
			public List<ColumnManager<VolumeData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<VolumeData>> result = new ArrayList<ColumnManager<VolumeData>>();

				if (selectedDataProvider.getScenarioInstances().size() > 1) {
					result.add(new ColumnManager<VolumeData>("Scenario") {

						@Override
						public String getColumnText(final VolumeData data) {
							final ScenarioInstance scenarioInstance = selectedDataProvider.getScenarioInstance(data.schedule);
							if (scenarioInstance != null) {
								return scenarioInstance.getName();
							}
							return null;
						}

						@Override
						public int compare(final VolumeData o1, final VolumeData o2) {
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

				result.add(new ColumnManager<VolumeData>("Contract") {
					@Override
					public String getColumnText(final VolumeData data) {
						return data.contract.getName();
					}

					@Override
					public int compare(final VolumeData o1, final VolumeData o2) {
						return o1.contract.getName().compareTo(o2.contract.getName());
					}
				});

				for (final int year : yearRange()) {
					result.add(new ColumnManager<VolumeData>(String.format("%d", year)) {
						@Override
						public String getColumnText(final VolumeData data) {
							final long result = data.volumes.containsKey(year) ? data.volumes.get(year) : 0;
							return String.format("%,d", result);
						}

						@Override
						public int compare(final VolumeData o1, final VolumeData o2) {
							final double result1 = o1.volumes.containsKey(year) ? o1.volumes.get(year) : 0;
							final double result2 = o2.volumes.containsKey(year) ? o2.volumes.get(year) : 0;
							return Double.compare(result1, result2);
						}
					});
				}

				return result;
			}

		};
	}

	@Override
	public void dispose() {
		overallVolumes.clear();
		super.dispose();
	}
}