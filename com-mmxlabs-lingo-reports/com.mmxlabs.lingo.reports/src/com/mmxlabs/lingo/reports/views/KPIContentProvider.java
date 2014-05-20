/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.lingo.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.BaseEntityBook;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EntityProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
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
import com.mmxlabs.scenario.service.model.ScenarioInstance;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * @since 3.0
 * 
 */
public class KPIContentProvider implements IStructuredContentProvider {

	public static final String LATENESS = "Lateness";
	private static final String IDLE = "Idle Time";
	private static final String TOTAL_COST = "Shipping Cost";
	private static final String TOTAL_PNL = "P&L";
	private static final String SHIPPING_PNL = "Shipping P&L";
	private static final String TRADING_PNL = "Trading P&L";
	private static final String MTM_PNL = "MtM P&L";

	public static final String TYPE_COST = "Cost";
	public static final String TYPE_TIME = "Days, hours";

	public static class RowData {
		public RowData(final String scheduleName, final String component, final String type, final long value, final String viewID, final boolean minimise) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.type = type;
			this.value = value;
			this.viewID = viewID;
			this.minimise = minimise;
		}

		public final String scheduleName;
		public final String component;
		public final String type;
		public final String viewID;
		public final long value;
		public final boolean minimise;

	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private void createRowData(final Schedule schedule, final ScenarioInstance scenarioInstance, final List<RowData> output) {

		long totalCost = 0l;
		long lateness = 0l;
		long totalTradingPNL = 0l;
		long totalShippingPNL = 0l;
		long totalMtMPNL = 0l;
		long totalIdleHours = 0l;

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

				if (evt instanceof SlotVisit) {
					final SlotVisit visit = (SlotVisit) evt;

					if (visit.getStart().after(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {

						final long late = visit.getStart().getTime() - visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime().getTime();
						lateness += (late / 1000 / 60 / 60);
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						final long late = evt.getStart().getTime() - vev.getVesselEvent().getStartBy().getTime();
						lateness += (late / 1000 / 60 / 60);
					}
				} else if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final VesselAvailability availability = seq.getVesselAvailability();
					if (availability == null) {
						continue;
					}
					if (seq.getEvents().indexOf(visit) == 0) {

						final Date startBy = availability.getStartBy();
						if (startBy != null && visit.getStart().after(startBy)) {
							final long late = visit.getStart().getTime() - startBy.getTime();
							lateness += (late / 1000 / 60 / 60);
						}
					} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
						final Date endBy = availability.getEndBy();
						if (endBy != null && visit.getStart().after(endBy)) {
							final long late = visit.getStart().getTime() - endBy.getTime();
							lateness += (late / 1000 / 60 / 60);
						}
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
			}
		}

		for (final OpenSlotAllocation openSlotAllocation : schedule.getOpenSlotAllocations()) {
			totalTradingPNL += getElementTradingPNL(openSlotAllocation);
			totalShippingPNL += getElementShippingPNL(openSlotAllocation);
		}

		for (final MarketAllocation marketAllocation : schedule.getMarketAllocations()) {
			totalMtMPNL += getElementTradingPNL(marketAllocation);
			totalMtMPNL += getElementShippingPNL(marketAllocation);
		}

		output.add(new RowData(scenarioInstance.getName(), TOTAL_PNL, TYPE_COST, totalTradingPNL + totalShippingPNL, TotalsHierarchyView.ID, false));
		output.add(new RowData(scenarioInstance.getName(), TRADING_PNL, TYPE_COST, totalTradingPNL, TotalsHierarchyView.ID, false));
		output.add(new RowData(scenarioInstance.getName(), SHIPPING_PNL, TYPE_COST, totalShippingPNL, TotalsHierarchyView.ID, false));
		output.add(new RowData(scenarioInstance.getName(), MTM_PNL, TYPE_COST, totalMtMPNL, TotalsHierarchyView.ID, false));

		output.add(new RowData(scenarioInstance.getName(), TOTAL_COST, TYPE_COST, totalCost, TotalsHierarchyView.ID, true));
		output.add(new RowData(scenarioInstance.getName(), LATENESS, TYPE_TIME, lateness, LatenessReportView.ID, true));
		output.add(new RowData(scenarioInstance.getName(), IDLE, TYPE_TIME, totalIdleHours, null, true));
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

	private final List<RowData> pinnedData = new ArrayList<RowData>();

	public List<RowData> getPinnedData() {
		return pinnedData;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
		rowData = new RowData[0];
		pinnedData.clear();
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					final ScenarioInstance scenarioInstance = synchOutput.getScenarioInstance(o);
					final boolean isPinned = synchOutput.isPinned(o);
					createRowData((Schedule) o, scenarioInstance, isPinned ? pinnedData : rowDataList);
					if (isPinned) {
						rowDataList.addAll(pinnedData);
					}
				}
			}
			rowData = rowDataList.toArray(rowData);
		}

	}

	@Override
	public void dispose() {

	}
}
