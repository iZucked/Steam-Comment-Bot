/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.ValueMode;
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
	public static IndexExposureData getExposuresByMonth(ScenarioResult scenarioResult, final @NonNull Schedule schedule, final @NonNull YearMonth date,
			final ValueMode mode, final Collection<Object> filterOn) {
		
		final Map<String, Double> result = new HashMap<>();
		final Map<String, Map<String, Double>> dealMap = new HashMap<>();

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
					if (detail.getDate().equals(date)) {
						final Map<String, Double> dealResult = dealMap.computeIfAbsent(slotAllocation.getName(), k -> new HashMap<>());
						
						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getIndexName(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getIndexName(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getIndexName(), detail.getNativeValue(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getNativeValue(), (a, b) -> (a + b));
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
					if (detail.getDate().equals(date)) {
						final Map<String, Double> dealResult = dealMap.computeIfAbsent(paperDeal.getName(), k -> new HashMap<>());

						switch (mode) {
						case VOLUME_MMBTU:
							result.merge(detail.getIndexName(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getVolumeInMMBTU(), (a, b) -> (a + b));
							break;
						case VOLUME_NATIVE:
							result.merge(detail.getIndexName(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
							break;
						case NATIVE_VALUE:
							result.merge(detail.getIndexName(), detail.getNativeValue(), (a, b) -> (a + b));
							dealResult.merge(detail.getIndexName(), detail.getNativeValue(), (a, b) -> (a + b));
							break;
						default:
							throw new IllegalArgumentException();
						}
					}
				}
			}
		}
		return new IndexExposureData(scenarioResult, schedule, date, result, dealMap);

	}
}
