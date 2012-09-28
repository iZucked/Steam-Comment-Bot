/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.StartEvent;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;
import com.mmxlabs.trading.optimiser.TradingConstants;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
public class KPIContentProvider implements IStructuredContentProvider {

	public static final String LATENESS = "Lateness";
	private static final String TOTAL_COST = "Shipping Cost";
	private static final String TOTAL_PNL = "P&L";

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

		final Set<String> validEntities = new HashSet<String>();
		final EObject instance = scenarioInstance.getInstance();
		if (instance instanceof MMXRootObject) {
			final MMXRootObject rootObject = (MMXRootObject) instance;
			final CommercialModel commercial = rootObject.getSubModel(CommercialModel.class);
			if (commercial != null) {
				for (final LegalEntity e : commercial.getEntities()) {
					// if (!TradingConstants.THIRD_PARTIES.equals(e.getName())) {
					validEntities.add(e.getName());
					// }
				}
			}
		}

		long totalCost = 0l;
		long lateness = 0;
		long totalPNL = 0l;

		for (final Sequence seq : schedule.getSequences()) {

			for (final Event evt : seq.getEvents()) {
				totalCost += evt.getHireCost();
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

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						totalPNL += getCargoPNL(cargoAllocation, validEntities);
					}

				} else if (evt instanceof VesselEventVisit) {
					final VesselEventVisit vev = (VesselEventVisit) evt;
					if (vev.getStart().after(vev.getVesselEvent().getStartBy())) {
						final long late = evt.getStart().getTime() - vev.getVesselEvent().getStartBy().getTime();
						lateness += (late / 1000 / 60 / 60);
					}
					totalPNL += getCargoPNL(vev, validEntities);
				} else if (evt instanceof StartEvent) {
					final StartEvent startEvent = (StartEvent) evt;
					totalPNL += getCargoPNL(startEvent, validEntities);
				} else if (evt instanceof EndEvent) {
					final EndEvent endEvent = (EndEvent) evt;
					totalPNL += getCargoPNL(endEvent, validEntities);
				} else if (evt instanceof GeneratedCharterOut) {
					final GeneratedCharterOut generatedCharterOut = (GeneratedCharterOut) evt;
					totalPNL += getCargoPNL(generatedCharterOut, validEntities);
				} else if (evt instanceof PortVisit) {
					final PortVisit visit = (PortVisit) evt;
					final Vessel vessel = seq.getVessel();
					if (vessel == null) {
						continue;
					}
					final VesselAvailability availability = vessel.getAvailability();
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
					// setInputEquivalents(visit, Collections.singleton((Object) visit.getSlotAllocation().getCargoAllocation()));
				}
			}
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}

		output.add(new RowData(scenarioInstance.getName(), TOTAL_COST, TYPE_COST, totalCost, TotalsHierarchyView.ID, true));
		if (totalPNL != 0) {
			output.add(new RowData(scenarioInstance.getName(), TOTAL_PNL, TYPE_COST, totalPNL, TotalsHierarchyView.ID, false));
		}
		output.add(new RowData(scenarioInstance.getName(), LATENESS, TYPE_TIME, lateness, LatenessReportView.ID, true));
	}

	private long getCargoPNL(final ExtraDataContainer allocation, final Set<String> validEntities) {
		long total = 0l;

		total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_upstream));
		total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_shipped));
		total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_downstream));

		return total;
	}

	// private long getOtherPNL(final Event event allocation, final Set<String> validEntities) {
	// long total = 0l;
	//
	// total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_upstream));
	// total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_shipped));
	// total += getExtraDataTotalPNL(validEntities, allocation.getDataWithKey(TradingConstants.ExtraData_downstream));
	//
	// return total;
	// }

	public long getExtraDataTotalPNL(final Set<String> validEntities, final ExtraData extraData) {

		long total = 0;
		if (extraData == null) {
			return total;
		}
		for (final ExtraData ed : extraData.getExtraData()) {
			if (validEntities.contains(ed.getName())) {
				final ExtraData data = ed.getDataWithKey(TradingConstants.ExtraData_pnl);
				if (data != null) {
					final Integer v = data.getValueAs(Integer.class);
					if (v != null) {
						total += v.longValue();
					}
				}
			}
		}
		return total;
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
					createRowData((Schedule) o, scenarioInstance, synchOutput.isPinned(o) ? pinnedData : rowDataList);
				}
			}
			rowData = rowDataList.toArray(rowData);
		}

	}

	@Override
	public void dispose() {

	}
}
