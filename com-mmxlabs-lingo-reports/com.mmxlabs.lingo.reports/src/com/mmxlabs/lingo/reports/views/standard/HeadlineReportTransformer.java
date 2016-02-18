/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.jdt.annotation.NonNull;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.util.ScheduleModelKPIUtils;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
class HeadlineReportTransformer {

	public static class RowData {
		public RowData(final String scheduleName, final Long totalPNL, final Long tradingPNL, final Long shippingPNL, final Long mtmPnl, final Long shippingCost, final Long idleTime,
				final Long gcoTime, final Long gcoRevenue, final Long capacityViolationCount, final Long latenessIncludingFlex, final Long latenessExcludingFlex) {
			super();
			this.scheduleName = scheduleName;
			this.totalPNL = totalPNL;
			this.tradingPNL = tradingPNL;
			this.shippingPNL = shippingPNL;
			this.mtmPnl = mtmPnl;
			this.shippingCost = shippingCost;
			this.idleTime = idleTime;
			this.gcoTime = gcoTime;
			this.gcoRevenue = gcoRevenue;
			this.capacityViolationCount = capacityViolationCount;
			this.latenessIncludingFlex = latenessIncludingFlex;
			this.latenessExcludingFlex = latenessExcludingFlex;
			this.dummy = false;
		}

		public RowData() {
			super();
			this.scheduleName = null;
			this.totalPNL = null;
			this.tradingPNL = null;
			this.shippingPNL = null;
			this.mtmPnl = null;
			this.shippingCost = null;
			this.idleTime = null;
			this.gcoTime = null;
			this.gcoRevenue = null;
			this.capacityViolationCount = null;
			this.latenessIncludingFlex = null;
			this.latenessExcludingFlex = null;
			this.dummy = true;
		}

		public final boolean dummy;
		public final String scheduleName;
		public final Long totalPNL;
		public final Long tradingPNL;
		public final Long shippingPNL;
		public final Long mtmPnl;
		public final Long shippingCost;
		public final Long idleTime;
		public final Long gcoTime;
		public final Long gcoRevenue;
		public final Long capacityViolationCount;
		public final Long latenessIncludingFlex;
		public final Long latenessExcludingFlex;
	}

	@NonNull
	public RowData transform(@NonNull final Schedule schedule, @NonNull final ScenarioInstance scenarioInstance) {

		long totalCost = 0L;

		long totalMtMPNL = 0L;
		long totalIdleHours = 0L;
		long totalGCOHours = 0L;
		long totalGCORevenue = 0L;

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {
				if (evt instanceof FuelUsage) {
					final FuelUsage mix = (FuelUsage) evt;
					// add up fuel components from mixture
					for (final FuelQuantity fq : mix.getFuels()) {
						totalCost += fq.getCost();
					}
				}
				if (evt instanceof Journey) {
					final Journey journey = (Journey) evt;
					totalCost += journey.getToll();
				}
				if (evt instanceof Idle) {
					final Idle idle = (Idle) evt;
					totalIdleHours += idle.getDuration();
				}
				if (evt instanceof PortVisit) {
					final int cost = ((PortVisit) evt).getPortCost();
					totalCost += cost;
				}

				if (evt instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) evt;
					totalGCOHours += evt.getDuration();
					totalGCORevenue += generatedCharterOut.getRevenue();
				}

			}

		}
		for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
			totalMtMPNL += ScheduleModelKPIUtils.getElementTradingPNL(marketAllocation);
			totalMtMPNL += ScheduleModelKPIUtils.getElementShippingPNL(marketAllocation);
		}

		final long[] scheduleProfitAndLoss = ScheduleModelKPIUtils.getScheduleProfitAndLossSplit(schedule);
		final long totalTradingPNL = scheduleProfitAndLoss[ScheduleModelKPIUtils.TRADING_PNL_IDX];
		final long totalShippingPNL = scheduleProfitAndLoss[ScheduleModelKPIUtils.SHIPPING_PNL_IDX];

		final int[] scheduleLateness = ScheduleModelKPIUtils.getScheduleLateness(schedule);
		final long totalLatenessHoursExcludingFlex = scheduleLateness[ScheduleModelKPIUtils.LATENESS_WITHOUT_FLEX_IDX];
		final long totalLatenessHoursIncludingFlex = scheduleLateness[ScheduleModelKPIUtils.LATENESS_WTH_FLEX_IDX];

		final long totalCapacityViolationCount = ScheduleModelKPIUtils.getScheduleViolationCount(schedule);

		return new RowData(scenarioInstance.getName(), totalTradingPNL + totalShippingPNL, totalTradingPNL, totalShippingPNL, totalMtMPNL, totalCost, totalIdleHours, totalGCOHours, totalGCORevenue,
				totalCapacityViolationCount, totalLatenessHoursIncludingFlex, totalLatenessHoursExcludingFlex);
	}
}
