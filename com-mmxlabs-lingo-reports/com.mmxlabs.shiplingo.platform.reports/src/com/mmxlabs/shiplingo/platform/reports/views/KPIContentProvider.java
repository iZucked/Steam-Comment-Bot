/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.Idle;
import com.mmxlabs.models.lng.schedule.Journey;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

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

		final EObject rootObject = scenarioInstance.getInstance();
		if (rootObject instanceof LNGScenarioModel) {
			final LNGScenarioModel lngScenarioModel = (LNGScenarioModel) rootObject;

			final ScenarioFleetModel scenarioFleetModel = lngScenarioModel.getPortfolioModel().getScenarioFleetModel();
			final Map<Vessel, VesselAvailability> vesselAvailabilityMap = new HashMap<Vessel, VesselAvailability>();
			for (final VesselAvailability vesselAvailability : scenarioFleetModel.getVesselAvailabilities()) {
				vesselAvailabilityMap.put(vesselAvailability.getVessel(), vesselAvailability);
			}

			long totalCost = 0l;
			long lateness = 0l;
			long totalPNL = 0l;
			long totalIdleHours = 0l;

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
						final Vessel vessel = seq.getVessel();
						if (vessel == null) {
							continue;
						}
						final VesselAvailability availability = vesselAvailabilityMap.get(vessel);
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
							totalPNL += getElementPNL(cargoAllocation);
						}

					} else if (evt instanceof ProfitAndLossContainer) {
						totalPNL += getElementPNL((ProfitAndLossContainer) evt);
					}
				}
			}

			if (totalPNL != 0) {
				output.add(new RowData(scenarioInstance.getName(), TOTAL_PNL, TYPE_COST, totalPNL, TotalsHierarchyView.ID, false));
			}
			output.add(new RowData(scenarioInstance.getName(), TOTAL_COST, TYPE_COST, totalCost, TotalsHierarchyView.ID, true));
			output.add(new RowData(scenarioInstance.getName(), LATENESS, TYPE_TIME, lateness, LatenessReportView.ID, true));
			output.add(new RowData(scenarioInstance.getName(), IDLE, TYPE_TIME, totalIdleHours, null, true));
		}
	}

	private long getElementPNL(final ProfitAndLossContainer container) {
		final long total = 0l;

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss != null) {
			return groupProfitAndLoss.getProfitAndLoss();
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
