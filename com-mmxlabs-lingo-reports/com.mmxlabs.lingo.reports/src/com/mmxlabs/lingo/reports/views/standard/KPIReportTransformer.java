/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
package com.mmxlabs.lingo.reports.views.standard;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

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
 * 
 */
public class KPIReportTransformer {

	@NonNull
	public static final String LATENESS = "Lateness";
	@NonNull
	private static final String IDLE = "Idle Time";
	@NonNull
	private static final String TOTAL_COST = "Shipping Cost";
	@NonNull
	private static final String TOTAL_PNL = "P&L";
	@NonNull
	private static final String SHIPPING_PNL = "Shipping P&L";
	@NonNull
	private static final String TRADING_PNL = "Trading P&L";
	@NonNull
	private static final String MTM_PNL = "MtM P&L";

	@NonNull
	public static final String TYPE_COST = "Cost";
	@NonNull
	public static final String TYPE_TIME = "Days, hours";

	public static class RowData {
		public RowData(final String scheduleName, final String component, final String type, final long value, final Long deltaValue, final String viewID, final boolean minimise) {
			super();
			this.scheduleName = scheduleName;
			this.component = component;
			this.type = type;
			this.value = value;
			this.deltaValue = deltaValue;

			this.viewID = viewID;
			this.minimise = minimise;
		}

		public final String scheduleName;
		public final String component;
		public final String type;
		public final String viewID;
		public final long value;
		public final Long deltaValue;
		public final boolean minimise;

	}

	public final List<RowData> transform(final Schedule schedule, final ScenarioInstance scenarioInstance, @Nullable final List<RowData> pinnedData) {
		final List<RowData> output = new LinkedList<>();
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
					if (visit.getStart().isAfter(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime())) {
						lateness += Hours.hoursBetween(visit.getSlotAllocation().getSlot().getWindowEndWithSlotOrPortTime(), visit.getStart()).getHours();
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					if (vev.getStart().isAfter(vev.getVesselEvent().getStartByAsDateTime())) {
						lateness += Hours.hoursBetween(vev.getVesselEvent().getStartByAsDateTime(), evt.getStart()).getHours();

					}
				} else if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final VesselAvailability availability = seq.getVesselAvailability();
					if (availability == null) {
						continue;
					}
					if (seq.getEvents().indexOf(visit) == 0) {

						final ZonedDateTime startBy = availability.getStartByAsDateTime();
						if (startBy != null && visit.getStart().isAfter(startBy)) {
							lateness += Hours.hoursBetween(startBy, visit.getStart()).getHours();
						}
					} else if (seq.getEvents().indexOf(visit) == seq.getEvents().size() - 1) {
						final ZonedDateTime endBy = availability.getEndAfterAsDateTime();
						if (endBy != null && visit.getStart().isAfter(endBy)) {
							lateness += Hours.hoursBetween(endBy, visit.getStart()).getHours();
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

		output.add(createRow(scenarioInstance.getName(), TOTAL_PNL, TYPE_COST, totalTradingPNL + totalShippingPNL, TotalsHierarchyView.ID, false, pinnedData));
		output.add(createRow(scenarioInstance.getName(), TRADING_PNL, TYPE_COST, totalTradingPNL, TotalsHierarchyView.ID, false, pinnedData));
		output.add(createRow(scenarioInstance.getName(), SHIPPING_PNL, TYPE_COST, totalShippingPNL, TotalsHierarchyView.ID, false, pinnedData));
		output.add(createRow(scenarioInstance.getName(), MTM_PNL, TYPE_COST, totalMtMPNL, TotalsHierarchyView.ID, false, pinnedData));

		output.add(createRow(scenarioInstance.getName(), TOTAL_COST, TYPE_COST, totalCost, TotalsHierarchyView.ID, true, pinnedData));
		output.add(createRow(scenarioInstance.getName(), LATENESS, TYPE_TIME, lateness, LatenessReportView.ID, true, pinnedData));
		output.add(createRow(scenarioInstance.getName(), IDLE, TYPE_TIME, totalIdleHours, null, true, pinnedData));

		return output;
	}

	private RowData createRow(final String scenarioInstanceName, @NonNull final String component, final String type, final long value, final String viewID, final boolean minimise,
			@Nullable final List<RowData> pinnedData) {
		return new RowData(scenarioInstanceName, component, type, value, getDelta(component, value, pinnedData), viewID, minimise);
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

	@Nullable
	private Long getDelta(@NonNull final String component, final long value, @Nullable final List<RowData> pinnedData) {
		if (pinnedData == null) {
			return null;
		}
		for (final RowData data : pinnedData) {
			if (component.equals(data.component)) {
				return data.value - value;
			}
		}
		return null;
	}
}
