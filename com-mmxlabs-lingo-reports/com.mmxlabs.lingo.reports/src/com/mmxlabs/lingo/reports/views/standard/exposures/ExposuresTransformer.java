/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard.exposures;

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.lingo.reports.views.standard.exposures.ExposureReportView.AssetType;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.commercial.parseutils.Exposures.ValueMode;
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
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;
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
			final ValueMode mode, final Collection<Object> filterOn, final String selectedEntity, final int selectedFiscalYear, final AssetType selectedAssetType) {
		
		final Map<String, Double> result = new HashMap<>();
		final Map<String, Map<String, Double>> dealMap = new HashMap<>();
		final List<IndexExposureData> children = new LinkedList<>();
		
		final IScenarioDataProvider sdp = scenarioResult.getScenarioDataProvider();
		final PricingModel pm = ScenarioModelUtil.getPricingModel(sdp);
		if (selectedAssetType != AssetType.PAPER) {
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
					final Slot s = slotAllocation.getSlot();
					String entity = "";
					if (s != null) {
						final BaseLegalEntity le = s.getEntity();
						if (le!= null) {
							entity = le.getName();
						}
					}
					if(selectedEntity != null) {
						if(!entity.equals(selectedEntity)) {
							continue;
						}
					}
					if(selectedFiscalYear != -1) {
						if(s.getWindowStart().getYear() != selectedFiscalYear) {
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
							String indexName = detail.getIndexName().equals("Physical") ? 
									detail.getIndexName() : ModelMarketCurveProvider.getMarketIndexName(pm, detail.getIndexName());
									if(indexName == null) {
										continue;
									}

									final Map<String, Double> dealResult = dealMap.computeIfAbsent(slotAllocation.getName(), k -> new HashMap<>());

									switch (mode) {
									case VOLUME_MMBTU:
										result.merge(indexName, detail.getVolumeInMMBTU(), (a, b) -> (a + b));
										dealResult.merge(indexName, detail.getVolumeInMMBTU(), (a, b) -> (a + b));
										break;
									case VOLUME_NATIVE:
										result.merge(indexName, detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
										dealResult.merge(indexName, detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
										break;
									case NATIVE_VALUE:
										result.merge(indexName, detail.getNativeValue(), (a, b) -> (a + b));
										dealResult.merge(indexName, detail.getNativeValue(), (a, b) -> (a + b));
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
				if (le!= null) {
					entity = le.getName();
				}
				if(selectedEntity != null) {
					if(!entity.equals(selectedEntity)) {
						continue;
					}
				}
				if(selectedFiscalYear != -1) {
					if (selectedFiscalYear != paperDeal.getYear()) {
						continue;
					}
				}

				for (final PaperDealAllocationEntry paperDealAllocationEntry : paperDealAllocation.getEntries()) {
					for (final ExposureDetail detail : paperDealAllocationEntry.getExposures()) {
						if (detail.getDate().equals(date)) {
							String indexName = ModelMarketCurveProvider.getMarketIndexName(pm, detail.getIndexName());
							if(indexName == null) {
								continue;
							}

							final Map<String, Double> dealResult = dealMap.computeIfAbsent(paperDeal.getName(), k -> new HashMap<>());
							switch (mode) {
							case VOLUME_MMBTU:
								result.merge(indexName, detail.getVolumeInMMBTU(), (a, b) -> (a + b));
								dealResult.merge(indexName, detail.getVolumeInMMBTU(), (a, b) -> (a + b));
								break;
							case VOLUME_NATIVE:
								result.merge(indexName, detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
								dealResult.merge(indexName, detail.getVolumeInNativeUnits(), (a, b) -> (a + b));
								break;
							case NATIVE_VALUE:
								result.merge(indexName, detail.getNativeValue(), (a, b) -> (a + b));
								dealResult.merge(indexName, detail.getNativeValue(), (a, b) -> (a + b));
								break;
							default:
								throw new IllegalArgumentException();
							}
						}
					}
				}
			}
		}
		return new IndexExposureData(scenarioResult, schedule, date, result, dealMap);
	}
}
