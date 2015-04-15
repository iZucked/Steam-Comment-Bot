/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.util.LatenessUtils;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
class HorizontalKPIContentProvider implements IStructuredContentProvider {

	public static class RowData {
		public RowData(final String scheduleName, final Long totalPNL, final Long tradingPNL, final Long shippingPNL, final Long mtmPnl, final Long shippingCost, final Long idleTime,
				final Long gcoTime, final Long gcoRevenue, final Long capacityViolationCount, final Long lateness) {
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
			this.lateness = lateness;
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
			this.lateness = null;
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
		public final Long lateness;
	}

	private final RowData[] rowData = new RowData[] { null, new RowData() };

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private RowData createRowData(final Schedule schedule, final ScenarioInstance scenarioInstance) {

		long totalCost = 0l;
		long totalTradingPNL = 0l;
		long totalShippingPNL = 0l;
		long totalMtMPNL = 0l;
		long totalIdleHours = 0l;
		long totalGCOHours = 0l;
		long totalGCORevenue = 0l;
		long totalCapacityViolationCount = 0l;
		long totalLatenessHours = 0l;

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

					if (LatenessUtils.isLate(evt)) {
						final long latenessInHours = LatenessUtils.getLatenessInHours((PortVisit) evt);
						// Ensure positive
						totalLatenessHours += Math.abs(latenessInHours);
					}
				}

				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						totalTradingPNL += getElementTradingPNL(cargoAllocation);
						totalShippingPNL += getElementShippingPNL(cargoAllocation);
					}

				} else if (evt instanceof ProfitAndLossContainer) {
					totalTradingPNL += getElementTradingPNL((ProfitAndLossContainer) evt);
					totalShippingPNL += getElementShippingPNL((ProfitAndLossContainer) evt);
				}

				if (evt instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) evt;
					totalGCOHours += evt.getDuration();
					totalGCORevenue += generatedCharterOut.getRevenue();
				}

				if (evt instanceof CapacityViolationsHolder) {
					final CapacityViolationsHolder capacityViolationsHolder = (CapacityViolationsHolder) evt;
					totalCapacityViolationCount += capacityViolationsHolder.getViolations().size();
				}
			}

		}
		for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
			totalMtMPNL += getElementTradingPNL(marketAllocation);
			totalMtMPNL += getElementShippingPNL(marketAllocation);
		}
		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			totalTradingPNL += getElementTradingPNL(openSlotAllocation);
			totalShippingPNL += getElementShippingPNL(openSlotAllocation);
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			object = object.eContainer();
		}

		return new RowData(scenarioInstance.getName(), totalTradingPNL + totalShippingPNL, totalTradingPNL, totalShippingPNL, totalMtMPNL, totalCost, totalIdleHours, totalGCOHours, totalGCORevenue,
				totalCapacityViolationCount, totalLatenessHours);
	}

	private long getElementShippingPNL(final ProfitAndLossContainer container) {
		return getElementPNL(container, CommercialPackage.Literals.BASE_LEGAL_ENTITY__SHIPPING_BOOK);
	}

	private long getElementTradingPNL(final ProfitAndLossContainer container) {
		return getElementPNL(container, CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK);
	}

	private long getElementPNL(final ProfitAndLossContainer container, final EStructuralFeature containmentFeature) {

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss != null) {
			long totalPNL = 0;
			for (final EntityProfitAndLoss entityPNL : groupProfitAndLoss.getEntityProfitAndLosses()) {
				final BaseEntityBook entityBook = entityPNL.getEntityBook();
				if (entityBook == null) {
					// Fall back code path for old models.
					if (containmentFeature == CommercialPackage.Literals.BASE_LEGAL_ENTITY__TRADING_BOOK) {
						return groupProfitAndLoss.getProfitAndLoss();
					} else {
						return 0;
					}
				} else {
					if (entityBook.eContainmentFeature() == containmentFeature) {
						totalPNL += entityPNL.getProfitAndLoss();
					}
				}
			}
			return totalPNL;
		}
		return 0;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

		rowData[0] = null;

		RowData newRowData = null;
		pinnedData = null;
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					final RowData rd = createRowData((Schedule) o, synchOutput.getScenarioInstance(o));
					// Exclude pin to avoid multiple rows....
					if (synchOutput.isPinned(o)) {
						pinnedData = rd;
					} else {
						assert newRowData == null;
						newRowData = rd;
					}
				}
			}
			// ...but add it in if it is the only row!
			if (newRowData == null && pinnedData != null) {
				newRowData = pinnedData;
			}
			rowData[0] = newRowData;
		}

		if (rowData[0] == null) {
			rowData[0] = new RowData("", null, null, null, null, null, null, null, null, null, null);
		}

	}

	private RowData pinnedData = new RowData("", null, null, null, null, null, null, null, null, null, null);

	public RowData getPinnedData() {
		return pinnedData;
	}

	@Override
	public void dispose() {

	}

}
