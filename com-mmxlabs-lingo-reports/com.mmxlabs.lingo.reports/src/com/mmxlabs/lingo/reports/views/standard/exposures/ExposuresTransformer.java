/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.exposures.ExposureEnumerations.AggregationMode;
import com.mmxlabs.common.exposures.ExposureEnumerations.ValueMode;
import com.mmxlabs.lingo.reports.views.standard.exposures.ExposureReportView.AssetType;
import com.mmxlabs.lingo.reports.views.standard.exposures.IndexExposureData.IndexExposureType;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.util.ModelMarketCurveProvider;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.scenario.service.ScenarioResult;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

public class ExposuresTransformer {

	/**
	 * Calculates the exposure of a given schedule to a given index. Depends on the getExposureCoefficient method to correctly determine the exposure per cubic metre associated with a load or
	 * discharge slot.
	 * 
	 * @param schedule
	 * @param index
	 * @param filterOn
	 * @return
	 */
	public static IndexExposureData getExposuresByMonth(ScenarioResult scenarioResult, final @NonNull Schedule schedule, final @NonNull YearMonth date, final ValueMode mode,
			final Collection<Object> filterOn, final String selectedEntity, final int selectedFiscalYear, final AssetType selectedAssetType, final boolean showGenerated) {

		final Map<String, Double> result = new HashMap<>();
		final Map<String, Map<String, Double>> dealMap = new HashMap<>();

		final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
		final PricingModel pm = ScenarioModelUtil.getPricingModel(sdp);
		if (selectedAssetType != AssetType.PAPER && selectedAssetType != AssetType.FINANCIAL) {
			for (final CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
				for (final SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
					if (!filterOn.isEmpty()) {
						final boolean include = filterOn.contains(slotAllocation) || filterOn.contains(slotAllocation.getSlot()) || filterOn.contains(cargoAllocation)
						// || filterOn.contains(cargoAllocation.getInputCargo())
						;
						if (!include) {
							continue;
						}
					}
					final Slot<?> s = slotAllocation.getSlot();
					String entity = "";
					if (s != null) {
						final BaseLegalEntity le = s.getEntity();
						if (le != null) {
							entity = le.getName();
						}
					}
					if (selectedEntity != null) {
						if (!entity.equals(selectedEntity)) {
							continue;
						}
					}
					if (selectedFiscalYear != -1) {
						if (s.getWindowStart() == null || (s.getWindowStart().getYear() != selectedFiscalYear)) {
							continue;
						}
					}

					for (final ExposureDetail detail : slotAllocation.getExposures()) {
						if (selectedAssetType == AssetType.PHYSICAL) {
							if (detail.getDealType() != DealType.PHYSICAL) {
								break;
							}
						}
						if (detail.getDate().equals(date)) {
							String indexName = detail.getIndexName().equalsIgnoreCase("Physical") ? detail.getIndexName() : ModelMarketCurveProvider.getMarketIndexName(pm, detail.getIndexName());
							if (indexName == null) {
								continue;
							}

							final Map<String, Double> dealResult = dealMap.computeIfAbsent(slotAllocation.getName(), k -> new HashMap<>());

							switch (mode) {
							case VOLUME_MMBTU:
								result.merge(indexName, detail.getVolumeInMMBTU(), Double::sum);
								dealResult.merge(indexName, detail.getVolumeInMMBTU(), Double::sum);
								break;
							case VOLUME_TBTU:
								result.merge(indexName, detail.getVolumeInMMBTU() / 1_000_000L, Double::sum);
								dealResult.merge(indexName, detail.getVolumeInMMBTU() / 1_000_000L, Double::sum);
								break;
							case VOLUME_NATIVE:
								result.merge(indexName, detail.getVolumeInNativeUnits(), Double::sum);
								dealResult.merge(indexName, detail.getVolumeInNativeUnits(), Double::sum);
								break;
							case NATIVE_VALUE:
								result.merge(indexName, detail.getNativeValue(), Double::sum);
								dealResult.merge(indexName, detail.getNativeValue(), Double::sum);
								break;
							default:
								throw new IllegalArgumentException();
							}
						}
					}
				}
			}
		}
		if (selectedAssetType == AssetType.NET || selectedAssetType == AssetType.FINANCIAL || selectedAssetType == AssetType.PAPER) {
			for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
				PaperDeal paperDeal = paperDealAllocation.getPaperDeal();
				if (paperDeal == null) {
					continue;
				}
				if (!showGenerated && schedule.getGeneratedPaperDeals().contains(paperDeal)) {
					continue;
				}
				if (!filterOn.isEmpty()) {
					final boolean include = filterOn.contains(paperDeal) || filterOn.contains(paperDealAllocation);
					if (!include) {
						continue;
					}
					if (!include) {
						continue;
					}
				}

				String entity = "";
				final BaseLegalEntity le = paperDeal.getEntity();
				if (le != null) {
					entity = le.getName();
				}
				if (selectedEntity != null) {
					if (!entity.equals(selectedEntity)) {
						continue;
					}
				}
				if (selectedFiscalYear != -1) {
					if (selectedFiscalYear != paperDeal.getYear()) {
						continue;
					}
				}

				for (final PaperDealAllocationEntry paperDealAllocationEntry : paperDealAllocation.getEntries()) {
					for (final ExposureDetail detail : paperDealAllocationEntry.getExposures()) {
						if (detail.getDate().equals(date)) {
							String indexName = ModelMarketCurveProvider.getMarketIndexName(pm, detail.getIndexName());
							if (indexName == null) {
								continue;
							}

							final Map<String, Double> dealResult = dealMap.computeIfAbsent(paperDeal.getName(), k -> new HashMap<>());
							switch (mode) {
							case VOLUME_MMBTU:
								result.merge(indexName, detail.getVolumeInMMBTU(), Double::sum);
								dealResult.merge(indexName, detail.getVolumeInMMBTU(), Double::sum);
								break;
							case VOLUME_TBTU:
								result.merge(indexName, detail.getVolumeInMMBTU() / 1_000_000L, Double::sum);
								dealResult.merge(indexName, detail.getVolumeInMMBTU() / 1_000_000L, Double::sum);
								break;
							case VOLUME_NATIVE:
								result.merge(indexName, detail.getVolumeInNativeUnits(), Double::sum);
								dealResult.merge(indexName, detail.getVolumeInNativeUnits(), Double::sum);
								break;
							case NATIVE_VALUE:
								result.merge(indexName, detail.getNativeValue(), Double::sum);
								dealResult.merge(indexName, detail.getNativeValue(), Double::sum);
								break;
							default:
								throw new IllegalArgumentException();
							}
						}
					}
				}
			}
		}
		return addDealSetData(new IndexExposureData(scenarioResult, schedule, date, result, dealMap), scenarioResult);
	}

	public static List<IndexExposureData> aggregateBy(final AggregationMode viewMode, final List<IndexExposureData> data, final @NonNull ScenarioResult scenarioResult) {
		if (data == null) {
			return new ArrayList<>();
		}

		switch (viewMode) {
		case BY_MONTH_NO_TOTAL:
			return data;
		case BY_MONTH:
			return addAnnualQuarterTotals(data);
		case BY_DEALSET:
			return reTransformByDealSet(data, scenarioResult);
		default:
			throw (new IllegalArgumentException());
		}
	}

	protected static IndexExposureData addDealSetData(final IndexExposureData ied, final @NonNull ScenarioResult scenarioResult) {
		final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		final List<DealSet> dealSets = cargoModel.getDealSets();

		if (ied.children == null) {
			return ied;
		}
		for (final IndexExposureData child : ied.children) {
			FOUND: for (final DealSet ds : dealSets) {
				final String dealSetName = ds.getName();
				for (final Slot<?> slot : ds.getSlots()) {
					if (slot.getName().equals(child.childName)) {
						child.setDealSet(dealSetName);
						continue FOUND;
					}
				}
				for (final PaperDeal paperDeal : ds.getPaperDeals()) {
					if (paperDeal.getName().equals(child.childName)) {
						child.setDealSet(dealSetName);
						continue FOUND;
					}
				}
			}
		}
		return ied;
	}

	protected static IndexExposureData mergeChildren(final IndexExposureData data1, final IndexExposureData data2) {
		if (data2 == null) {
			return data1;
		}
		if (data1 == null) {
			return data2;
		}
		data2.exposures.entrySet().forEach(e -> {
			data1.exposures.merge(e.getKey(), e.getValue(), Double::sum);
		});
		for (final IndexExposureData ied1 : data1.children) {
			Iterator<IndexExposureData> iedIterator = data2.children.iterator();
			while (iedIterator.hasNext()) {
				IndexExposureData ied2 = iedIterator.next();
				if (ied1.childName.equals(ied2.childName)) {
					ied2.exposures.entrySet().forEach(e -> ied1.exposures.merge(e.getKey(), e.getValue(), Double::sum));
					iedIterator.remove();
				}
			}
		}
		data1.children.addAll(data2.children);
		return data1;
	}

	protected static void mergeExposures(final IndexExposureData data1, final IndexExposureData data2) {
		if (data2 == null) {
			return;
		}
		if (data2.exposures.isEmpty()) {
			return;
		}
		data2.exposures.entrySet().forEach(e -> data1.exposures.merge(e.getKey(), e.getValue(), Double::sum));
	}

	protected static List<IndexExposureData> addAnnualQuarterTotals(final List<IndexExposureData> output) {
		if (output.isEmpty()) {
			return output;
		}

		int firstYear = output.get(0).year;
		int lastYear = output.get(output.size() - 1).year;

		List<IndexExposureData> result = new LinkedList<>();

		for (int i = firstYear; i < lastYear + 1; i++) {
			final int ifin = i;

			IndexExposureData ied = null;
			Iterator<IndexExposureData> foo1 = output.stream().filter(a -> a.date.getYear() == ifin).iterator();
			while (foo1.hasNext()) {
				if (ied == null) {
					ied = new IndexExposureData(foo1.next());
					ied.setType(IndexExposureType.ANNUAL);
				} else {
					mergeExposures(ied, foo1.next());
				}
			}
			result.add(ied);

			for (int j = 1; j < 4 + 1; j++) {
				final int jfin = j;
				IndexExposureData jed = null;
				Iterator<IndexExposureData> foo2 = output.stream() //
						.filter(a -> a.year == ifin && a.quarter == jfin) //
						.iterator();
				while (foo2.hasNext()) {
					if (jed == null) {
						jed = new IndexExposureData(foo2.next());
						jed.setType(IndexExposureType.QUARTERLY);
					} else {
						mergeExposures(jed, foo2.next());
					}
				}
				if (jed != null) {
					result.add(jed);
				}
			}

			result.addAll(output.stream() //
					.filter(a -> a.date.getYear() == ifin) //
					.collect(Collectors.toList()));
		}
		return result;
	}

	protected static List<IndexExposureData> reTransformByDealSet(final List<IndexExposureData> data, final @NonNull ScenarioResult scenarioResult) {
		if (data.isEmpty()) {
			return data;
		}

		final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
		final Schedule schedule = ScenarioModelUtil.getScheduleModel(sdp).getSchedule();
		final CargoModel cargoModel = ScenarioModelUtil.getCargoModel(sdp);
		final List<DealSet> dealSets = cargoModel.getDealSets();
		final List<IndexExposureData> result = new ArrayList<>();

		for (final DealSet ds : dealSets) {
			final String dealSetName = ds.getName();
			final IndexExposureData iedFoo = new IndexExposureData(scenarioResult, schedule, dealSetName);

			for (final IndexExposureData ied : data) {
				if (ied.children == null)
					continue;
				for (final IndexExposureData child : ied.children) {
					if (dealSetName.equals(child.dealSet)) {

						final IndexExposureData iedTemp;
						List<IndexExposureData> iedTempList = iedFoo.children.stream().filter(a -> a.date == ied.date).collect(Collectors.toList());
						if (iedTempList.isEmpty()) {
							iedTemp = new IndexExposureData(ied);
						} else {
							iedTemp = iedTempList.get(0);
						}
						iedTemp.setType(IndexExposureType.MONTHLY);
						iedTemp.children.add(child);
						if (iedTempList.isEmpty()) {
							iedFoo.children.add(iedTemp);
						}
					}
				}
			}
			reCalculateChildExposures(iedFoo);
			result.add(iedFoo);
		}

		return result;
	}

	private static void reCalculateChildExposures(final IndexExposureData child) {
		if (child.children != null) {
			final Map<String, Double> cExposures = new HashMap<>();
			for (final IndexExposureData cChild : child.children) {
				if (cChild.children != null) {
					reCalculateChildExposures(cChild);
				}
				if (cChild.exposures.isEmpty()) {
					continue;
				}
				for (final Map.Entry<String, Double> entry : cChild.exposures.entrySet()) {
					cExposures.merge(entry.getKey(), entry.getValue(), Double::sum);
				}
			}
			child.exposures.clear();
			child.exposures.putAll(cExposures);
		}
	}

	protected static boolean inspectChildrenAndExposures(final IndexExposureData fd) {
		if (fd == null) {
			return false;
		}
		if (fd.children != null && !fd.children.isEmpty()) {
			return true;
		}
		if (fd.exposures != null && !fd.exposures.isEmpty()) {
			return true;
		}
		return false;
	}
}
