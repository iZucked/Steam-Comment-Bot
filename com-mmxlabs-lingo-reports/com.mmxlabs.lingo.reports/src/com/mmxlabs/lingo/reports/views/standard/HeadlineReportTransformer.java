/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotAllocationType;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.model.manager.ScenarioModelRecord;
import com.mmxlabs.scenario.service.ui.ScenarioResult;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
class HeadlineReportTransformer {

	public static class RowData {

		public RowData(final String scheduleName, final Long totalPNL, final Long tradingPNL, final Long shippingPNL, final Long upstreamDownstreamPNL, final Long mtmPnl, final Long idleTime,
				final Long gcoTime, final Long gcoRevenue, final Long capacityViolationCount, final Long latenessIncludingFlex, final Long latenessExcludingFlex, Long purchaseCost,
				Long salesRevenue) {
			super();
			this.scheduleName = scheduleName;
			this.totalPNL = totalPNL;
			this.tradingPNL = tradingPNL;
			this.shippingPNL = shippingPNL;
			this.upstreamDownstreamPNL = upstreamDownstreamPNL;
			this.mtmPnl = mtmPnl;
			this.idleTime = idleTime;
			this.gcoTime = gcoTime;
			this.gcoRevenue = gcoRevenue;
			this.capacityViolationCount = capacityViolationCount;
			this.latenessIncludingFlex = latenessIncludingFlex;
			this.latenessExcludingFlex = latenessExcludingFlex;
			this.purchaseCost = purchaseCost;
			this.salesRevenue = salesRevenue;
			this.dummy = false;
		}

		public RowData() {
			super();
			this.scheduleName = "";
			this.totalPNL = null;
			this.tradingPNL = null;
			this.shippingPNL = null;
			this.upstreamDownstreamPNL = null;
			this.mtmPnl = null;
			this.idleTime = null;
			this.gcoTime = null;
			this.gcoRevenue = null;
			this.capacityViolationCount = null;
			this.latenessIncludingFlex = null;
			this.latenessExcludingFlex = null;
			this.salesRevenue = null;
			this.purchaseCost = null;
			this.dummy = true;
		}

		public final boolean dummy;
		public final String scheduleName;
		public final Long totalPNL;
		public final Long tradingPNL;
		public final Long shippingPNL;
		public final Long upstreamDownstreamPNL;
		public final Long mtmPnl;
		// public final Long shippingCost;
		public final Long idleTime;
		public final Long gcoTime;
		public final Long gcoRevenue;
		public final Long capacityViolationCount;
		public final Long latenessIncludingFlex;
		public final Long latenessExcludingFlex;
		public final Long salesRevenue;
		public final Long purchaseCost;
	}

	@NonNull
	public RowData transform(@NonNull final Schedule schedule, @NonNull final ScenarioResult scenarioResult) {

		long totalSalesRevenue = 0L;
		long totalPurchaseCost = 0L;
		long totalMtMPNL = 0L;
		long totalIdleHours = 0L;
		long totalGCOHours = 0L;
		long totalGCORevenue = 0L;

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {

				if (evt instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) evt;
					totalGCOHours += evt.getDuration();
					totalGCORevenue += generatedCharterOut.getRevenue();
				}

				if (evt instanceof Idle) {
					final Idle idle = (Idle) evt;
					totalIdleHours += idle.getDuration();
				}
			}
		}
		for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
			totalMtMPNL += ScheduleModelKPIUtils.getElementTradingPNL(marketAllocation);
			totalMtMPNL += ScheduleModelKPIUtils.getElementShippingPNL(marketAllocation);
		}

		for (CargoAllocation cargoAllocation : schedule.getCargoAllocations()) {
			for (SlotAllocation slotAllocation : cargoAllocation.getSlotAllocations()) {
				if (slotAllocation.getSlotAllocationType() == SlotAllocationType.PURCHASE) {
					totalPurchaseCost += slotAllocation.getVolumeValue();
				} else {
					totalSalesRevenue += slotAllocation.getVolumeValue();

				}
			}
		}

		final long[] scheduleProfitAndLoss = ScheduleModelKPIUtils.getScheduleProfitAndLossSplit(schedule);
		final long totalTradingPNL = scheduleProfitAndLoss[ScheduleModelKPIUtils.TRADING_PNL_IDX];
		final long totalShippingPNL = scheduleProfitAndLoss[ScheduleModelKPIUtils.SHIPPING_PNL_IDX];
		final long totalUpstreamPNL = scheduleProfitAndLoss[ScheduleModelKPIUtils.UPSTREAM_PNL_IDX];

		final int[] scheduleLateness = ScheduleModelKPIUtils.getScheduleLateness(schedule);
		final long totalLatenessHoursExcludingFlex = scheduleLateness[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		final long totalLatenessHoursIncludingFlex = scheduleLateness[ScheduleModelKPIUtils.LATENESS_WTH_FLEX_IDX];

		final long totalCapacityViolationCount = ScheduleModelKPIUtils.getScheduleViolationCount(schedule);

		ScenarioModelRecord modelRecord = scenarioResult.getModelRecord();
		return new RowData(modelRecord.getName(), totalTradingPNL + totalShippingPNL + totalUpstreamPNL, totalTradingPNL, totalShippingPNL, totalUpstreamPNL, totalMtMPNL, totalIdleHours,
				totalGCOHours, totalGCORevenue, totalCapacityViolationCount, totalLatenessHoursIncludingFlex, totalLatenessHoursExcludingFlex, totalPurchaseCost, totalSalesRevenue);
	}
}
