/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.LocalDate;
import java.time.Year;
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
import org.eclipse.jface.action.Action;
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
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Class to display a report describing the total volume of gas transactions for each contract (purchase & sales) for each gas year (with additional rows for "other purchase" and "other sales").
 * 
 * @author Simon McGregor
 */
public class VolumeTrackingReportView extends SimpleTabularReportView<VolumeTrackingReportView.VolumeData> {

	private final Pair<Year, Year> dateRange = new Pair<>();

	private enum ValueMode {
		VOLUME_MMBTU, VOLUME_M3, /* VOLUME_NATIVE -- link to exposures calcs, needs volume unit set on contract */
	}

	private ValueMode mode = ValueMode.VOLUME_MMBTU;

	public VolumeTrackingReportView() {
		super("com.mmxlabs.lingo.doc.Reports_VolumeTracking");
	}

	public static class VolumeData {
		public final ScenarioResult scenarioResult;
		public final String contract;
		public final Map<Year, Long> volumes;
		public final Schedule schedule;
		private final boolean purchase;

		public VolumeData(ScenarioResult scenarioResult, final Schedule schedule, final boolean purchase, final String contract, final Map<Year, Long> volumes) {
			this.scenarioResult = scenarioResult;
			this.schedule = schedule;
			this.purchase = purchase;
			this.contract = contract;
			this.volumes = volumes;
		}
	}

	/**
	 * Returns the gas year for a particular calendar date. The gas year starts in October and is based on UTC.
	 * 
	 * @param calendar
	 * @return
	 */
	private Year getGasYear(final ZonedDateTime calendar) {
		final LocalDate utc = calendar.toLocalDate();

		// subtract one year from the reported year for dates prior to october
		final int yearOffset = (utc.getMonthValue() < Calendar.OCTOBER) ? -1 : 0;

		return Year.of(utc.getYear() + yearOffset);
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
			public @NonNull List<@NonNull VolumeData> createData(@Nullable final Pair<@NonNull Schedule, @NonNull ScenarioResult> pinnedPair,
					@NonNull final List<@NonNull Pair<@NonNull Schedule, @NonNull ScenarioResult>> otherPairs) {
				dateRange.setBoth(null, null);

				final List<@NonNull VolumeData> output = new LinkedList<>();

				if (pinnedPair != null && otherPairs.size() == 1) {
					// Pin/Diff mode
					final List<VolumeData> ref = createData(pinnedPair.getFirst(), pinnedPair.getSecond());

					final Pair<@NonNull Schedule, @NonNull ScenarioResult> p = otherPairs.get(0);
					final List<VolumeData> other = createData(p.getFirst(), p.getSecond());

					LOOP_REF_DATA: for (final VolumeData refData : ref) {
						final Iterator<VolumeData> otherIterator = other.iterator();
						while (otherIterator.hasNext()) {
							final VolumeData otherData = otherIterator.next();
							if (Equality.isEqual(refData.contract, otherData.contract) && refData.purchase == otherData.purchase) {

								output.add(createDiffData(refData, otherData));
								otherIterator.remove();
								continue LOOP_REF_DATA;
							}
						}
						output.add(createDiffData(refData, null));

					}
					for (final VolumeData otherData : other) {
						output.add(createDiffData(null, otherData));

					}
				} else {
					if (pinnedPair != null) {
						output.addAll(createData(pinnedPair.getFirst(), pinnedPair.getSecond()));
					}
					for (final Pair<@NonNull Schedule, @NonNull ScenarioResult> p : otherPairs) {
						output.addAll(createData(p.getFirst(), p.getSecond()));
					}
				}
				for (final VolumeData d : output) {
					for (final Year ym : d.volumes.keySet()) {
						if (dateRange.getFirst() == null || ym.isBefore(dateRange.getFirst())) {
							dateRange.setFirst(ym);
						}
						if (dateRange.getSecond() == null || ym.isAfter(dateRange.getSecond())) {
							dateRange.setSecond(ym);
						}
					}
				}

				return output;
			}

			public List<@NonNull VolumeData> createData(final Schedule schedule, final ScenarioResult scenarioResult) {
				final List<@NonNull VolumeData> output = new ArrayList<>();
				final Map<String, Map<Year, Long>> purchaseVolumes = new HashMap<>();
				final Map<String, Map<Year, Long>> salesVolumes = new HashMap<>();

				for (final CargoAllocation ca : schedule.getCargoAllocations()) {
					for (final SlotAllocation sa : ca.getSlotAllocations()) {
						final long volume;
						switch (mode) {
						case VOLUME_M3:
							volume = sa.getVolumeTransferred();
							break;
						case VOLUME_MMBTU:
							volume = sa.getEnergyTransferred();
							break;
						default:
							throw new IllegalArgumentException();
						}

						final Contract contract = sa.getContract();
						final String contractName;
						if (contract == null) {
							final Slot slot = sa.getSlot();
							if (slot instanceof LoadSlot) {
								contractName = "Other purchases";
							} else if (slot instanceof DischargeSlot) {
								contractName = "Other sales";
							} else {
								contractName = "Unknown";
								assert false;
							}
						} else {
							contractName = contract.getName();
						}
						assert contractName != null;
						final Year year = getGasYear(sa.getSlotVisit().getStart());
						if (sa.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
							final Map<Year, Long> m = purchaseVolumes.getOrDefault(contractName, new HashMap<>());
							final long newValue = m.getOrDefault(year, 0L) + volume;
							m.put(year, newValue);
							purchaseVolumes.put(contractName, m);
						} else if (sa.getSlotAllocationType() == SlotAllocationType.SALE) {
							final Map<Year, Long> m = salesVolumes.getOrDefault(contractName, new HashMap<>());
							final long newValue = m.getOrDefault(year, 0L) + volume;
							m.put(year, newValue);
							salesVolumes.put(contractName, m);
						}
					}
				}
				for (final Map.Entry<String, Map<Year, Long>> e : purchaseVolumes.entrySet()) {
					output.add(new VolumeData(scenarioResult, schedule, true, e.getKey(), e.getValue()));
				}
				for (final Map.Entry<String, Map<Year, Long>> e : salesVolumes.entrySet()) {
					output.add(new VolumeData(scenarioResult, schedule, false, e.getKey(), e.getValue()));
				}

				return output;
			}

			protected @NonNull VolumeData createDiffData(final VolumeData pinData, final VolumeData otherData) {

				final VolumeData modelData = pinData != null ? pinData : otherData;
				final Map<Year, Long> volumes = new HashMap<Year, Long>();
				final VolumeData newData = new VolumeData(null, null, modelData.purchase, modelData.contract, volumes);

				if (pinData != null) {
					for (final Map.Entry<Year, Long> e : pinData.volumes.entrySet()) {
						final long val = volumes.getOrDefault(e.getKey(), 0L);
						volumes.put(e.getKey(), val + e.getValue());
					}
				}
				if (otherData != null) {
					for (final Map.Entry<Year, Long> e : otherData.volumes.entrySet()) {
						final long val = volumes.getOrDefault(e.getKey(), 0L);
						volumes.put(e.getKey(), val - e.getValue());
					}
				}

				return newData;
			}

			@Override
			public List<ColumnManager<VolumeData>> getColumnManagers(@NonNull final ISelectedDataProvider selectedDataProvider) {
				final ArrayList<ColumnManager<VolumeData>> result = new ArrayList<ColumnManager<VolumeData>>();

				if (selectedDataProvider.getScenarioResults().size() > 1) {
					result.add(new ColumnManager<VolumeData>("Scenario") {

						@Override
						public String getColumnText(final VolumeData data) {
							final ScenarioResult scenarioResult = data.scenarioResult;
							if (scenarioResult != null) {
								ScenarioInstance scenarioInstance = scenarioResult.getScenarioInstance();
								if (scenarioInstance != null) {
									return scenarioInstance.getName();
								}
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
						return data.contract;
					}

					@Override
					public int compare(final VolumeData o1, final VolumeData o2) {
						return o1.contract.compareTo(o2.contract);
					}
				});

				if (dateRange != null && dateRange.getFirst() != null) {
					Year year = dateRange.getFirst();
					while (!year.isAfter(dateRange.getSecond())) {
						final Year fYear = year;
						result.add(new ColumnManager<VolumeData>(String.format("%d", fYear.getValue())) {
							@Override
							public String getColumnText(final VolumeData data) {
								final long result = data.volumes.containsKey(fYear) ? data.volumes.get(fYear) : 0;
								return String.format("%,d", result);
							}

							@Override
							public int compare(final VolumeData o1, final VolumeData o2) {
								final double result1 = o1.volumes.containsKey(fYear) ? o1.volumes.get(fYear) : 0;
								final double result2 = o2.volumes.containsKey(fYear) ? o2.volumes.get(fYear) : 0;
								return Double.compare(result1, result2);
							}
						});
						year = year.plusYears(1);
					}
				}

				return result;
			}

		};
	}

	protected void makeActions() {
		super.makeActions();

		final Action modeToggle = new Action("Volume:", Action.AS_PUSH_BUTTON) {
			public void run() {

				final int modeIdx = (mode.ordinal() + 1) % ValueMode.values().length;
				mode = ValueMode.values()[modeIdx];
				setActionText(this, mode);
				getViewSite().getActionBars().updateActionBars();
				VolumeTrackingReportView.this.refresh();

			}
		};
		setActionText(modeToggle, mode);

		getViewSite().getActionBars().getToolBarManager().add(modeToggle);

		getViewSite().getActionBars().getToolBarManager().update(true);
	}

	private void setActionText(final Action a, final ValueMode mode) {
		String modeStr = null;
		switch (mode) {
		case VOLUME_MMBTU:
			modeStr = "mmBtu";
			break;
		case VOLUME_M3:
			modeStr = "m³";
			break;
		default:
			assert false;
			break;

		}
		assert modeStr != null;
		a.setText("Volume: " + modeStr);
	}
}