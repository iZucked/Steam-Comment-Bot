/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
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
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
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
				final Long gcoTime, final Long capacityViolationCount, final Long lateness) {
			super();
			this.scheduleName = scheduleName;
			this.totalPNL = totalPNL;
			this.tradingPNL = tradingPNL;
			this.shippingPNL = shippingPNL;
			this.mtmPnl = mtmPnl;
			this.shippingCost = shippingCost;
			this.idleTime = idleTime;
			this.gcoTime = gcoTime;
			this.capacityViolationCount = capacityViolationCount;
			this.lateness = lateness;
		}

		public final String scheduleName;
		public Long totalPNL;
		public final Long tradingPNL;
		public final Long shippingPNL;
		public final Long mtmPnl;
		public final Long shippingCost;
		public final Long idleTime;
		public final Long gcoTime;
		public final Long capacityViolationCount;
		public final Long lateness;
	}

	private RowData[] rowData = new RowData[0];

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
						final PortVisit slotVisit = (PortVisit) evt;
						final Calendar localStart = slotVisit.getLocalStart();
						final Calendar windowEndDate = getWindowEndDate(slotVisit);

						long diff = localStart.getTimeInMillis() - windowEndDate.getTimeInMillis();

						// Strip milliseconds
						diff /= 1000;
						// Strip seconds;
						diff /= 60;
						// Strip minutes
						diff /= 60;
						// Ensure positive
						totalLatenessHours += Math.abs(diff);
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
					totalGCOHours += evt.getDuration();
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

		return new RowData(scenarioInstance.getName(), totalTradingPNL + totalShippingPNL, totalTradingPNL, totalShippingPNL, totalMtMPNL, totalCost, totalIdleHours, totalGCOHours,
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

		rowData = new RowData[0];

		pinnedData = null;
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					final RowData rd = createRowData((Schedule) o, synchOutput.getScenarioInstance(o));
					// Exclude pin to avoid multiple rows....
					if (synchOutput.isPinned(o)) {
						pinnedData = rd;
					} else {
						rowDataList.add(rd);
					}
				}
			}
			// ...but add it in if it is the only row!
			if (rowDataList.isEmpty() && pinnedData != null) {
				rowDataList.add(pinnedData);
			}
			rowData = rowDataList.toArray(rowData);
		}

		if (rowData.length == 0) {
			rowData = new RowData[] { new RowData("", null, null, null, null, null, null, null, null, null) };
		}

	}

	private RowData pinnedData = new RowData("", null, null, null, null, null, null, null, null, null);

	public RowData getPinnedData() {
		return pinnedData;
	}

	@Override
	public void dispose() {

	}

	private Calendar getWindowEndDate(final Object object) {
		final Date date;
		if (object instanceof SlotVisit) {
			date = ((SlotVisit) object).getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime();
			String timeZone = ((SlotVisit) object).getSlotAllocation().getSlot().getTimeZone(CargoPackage.eINSTANCE.getSlot_WindowStart());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof VesselEventVisit) {
			date = ((VesselEventVisit) object).getVesselEvent().getStartBy();
			String timeZone = ((VesselEventVisit) object).getVesselEvent().getTimeZone(CargoPackage.eINSTANCE.getVesselEvent_StartBy());
			if (timeZone == null)
				timeZone = "UTC";
			final Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
			c.setTime(date);
			return c;
		} else if (object instanceof PortVisit) {
			final PortVisit visit = (PortVisit) object;
			final Sequence seq = visit.getSequence();
			final VesselAvailability vesselAvailability = seq.getVesselAvailability();
			if (vesselAvailability == null) {
				return null;
			}
			if (seq.getEvents().indexOf(visit) == 0) {
				final Date startBy = vesselAvailability.getStartBy();
				if (startBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(startBy);
					return c;
				}
			} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
				final Date endBy = vesselAvailability.getEndBy();
				if (endBy != null) {
					final Calendar c = Calendar.getInstance();
					c.setTime(endBy);
					return c;
				}
			}
		}
		return null;
	}

}
