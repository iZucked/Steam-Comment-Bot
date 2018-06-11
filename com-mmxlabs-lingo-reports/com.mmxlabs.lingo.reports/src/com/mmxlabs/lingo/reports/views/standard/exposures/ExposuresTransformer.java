/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.ValueMode;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocationEntry;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.DealType;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

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
	public static IndexExposureData getExposuresByMonth(ScenarioResult scenarioResult, final @NonNull Schedule schedule, final @NonNull CommodityIndex index, @NonNull final PricingModel pricingModel,
			final ValueMode mode, final Collection<Object> filterOn) {
		final Map<YearMonth, Double> result = new HashMap<>();

		final Map<Pair<DealType, String>, Map<YearMonth, Double>> dealMap = new HashMap<>();

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

				for (final ExposureDetail detail : slotAllocation.getExposures()) {
					if (Objects.equals(detail.getIndexName(), index.getName())) {
						final Map<YearMonth, Double> dealResult = dealMap.computeIfAbsent(new Pair<>(detail.getDealType(), slotAllocation.getName()), k -> new HashMap<>());

						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}
		for (final PaperDealAllocation paperDealAllocation : schedule.getPaperDealAllocations()) {
			for (final PaperDealAllocationEntry paperDealAllocationEntry : paperDealAllocation.getEntries()) {
				PaperDeal paperDeal = paperDealAllocation.getPaperDeal();
				if (paperDeal == null) {
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

				for (final ExposureDetail detail : paperDealAllocationEntry.getExposures()) {
					if (Objects.equals(detail.getIndexName(), index.getName())) {
						final Map<YearMonth, Double> dealResult = dealMap.computeIfAbsent(new Pair<>(detail.getDealType(), paperDeal.getName()), k -> new HashMap<>());

						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}

		return new IndexExposureData(scenarioResult, schedule, index.getName(), index, result, dealMap, index.getCurrencyUnit(), index.getVolumeUnit());

	}

	public static IndexExposureData getPhysicalExposuresByMonth(ScenarioResult scenarioResult, final @NonNull Schedule schedule, @NonNull final PricingModel pricingModel, final ValueMode mode,
			final Collection<Object> filterOn) {
		final Map<YearMonth, Double> result = new HashMap<>();

		final Map<Pair<DealType, String>, Map<YearMonth, Double>> dealMap = new HashMap<>();

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

				for (final ExposureDetail detail : slotAllocation.getExposures()) {
					if (detail.getDealType() == DealType.PHYSICAL) {
						final Map<YearMonth, Double> dealResult = dealMap.computeIfAbsent(new Pair<>(detail.getDealType(), slotAllocation.getName()), k -> new HashMap<>());

						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							dealResult.merge(detail.getDate(), detail.getNativeValue(), (a, b) -> (a + b));
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}

		return new IndexExposureData(scenarioResult, schedule, "Physical", null, result, dealMap, "", "mmBtu");

	}
}
