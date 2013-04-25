/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.reports.views;

import java.util.ArrayList;
import java.util.EventObject;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.mmxlabs.models.lng.cargo.LoadSlot;
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
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.shiplingo.platform.reports.IScenarioViewerSynchronizerOutput;

/**
 * Content provider for the {@link CargoReportView}.
 * 
 * @author Simon Goodall
 * 
 */
class HorizontalKPIContentProvider implements IStructuredContentProvider {

	private final CommandStackListener commandStackListener = new CommandStackListener() {

		@Override
		public void commandStackChanged(final EventObject event) {
			currentViewer.setInput(currentViewer.getInput());
		}
	};

	private CommandStack currentCommandStack;
	private Viewer currentViewer;

	public static class RowData {
		public RowData(final String scheduleName, final Long pnl, final Long shippingCost, final Long idleTime) {
			super();
			this.scheduleName = scheduleName;
			this.pnl = pnl;
			this.shippingCost = shippingCost;
			this.idleTime = idleTime;
		}

		public final String scheduleName;
		public final Long pnl;
		public final Long shippingCost;
		public final Long idleTime;
	}

	private RowData[] rowData = new RowData[0];

	@Override
	public Object[] getElements(final Object inputElement) {
		return rowData;
	}

	private RowData createRowData(final Schedule schedule, final ScenarioInstance scenarioInstance) {

		long totalCost = 0l;
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

					if (visit.getSlotAllocation().getSlot() instanceof LoadSlot) {
						final CargoAllocation cargoAllocation = visit.getSlotAllocation().getCargoAllocation();
						totalPNL += getElementPNL(cargoAllocation);
					}

				} else if (evt instanceof ProfitAndLossContainer) {
					totalPNL += getElementPNL((ProfitAndLossContainer) evt);
				}
			}
		}

		EObject object = schedule.eContainer();
		while ((object != null) && !(object instanceof MMXRootObject)) {
			if (object instanceof EObject) {
				object = ((EObject) object).eContainer();
			}
		}

		return new RowData(scenarioInstance.getName(), totalPNL, totalCost, totalIdleHours);
	}

	private long getElementPNL(final ProfitAndLossContainer container) {

		final GroupProfitAndLoss groupProfitAndLoss = container.getGroupProfitAndLoss();
		if (groupProfitAndLoss != null) {
			return groupProfitAndLoss.getProfitAndLoss();
		}
		return 0;
	}

	@Override
	public synchronized void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {

		if (currentCommandStack != null) {
			currentCommandStack.removeCommandStackListener(commandStackListener);
		}
		currentViewer = viewer;

		rowData = new RowData[0];

		pinnedData = null;
		if (newInput instanceof IScenarioViewerSynchronizerOutput) {
			final IScenarioViewerSynchronizerOutput synchOutput = (IScenarioViewerSynchronizerOutput) newInput;
			final ArrayList<RowData> rowDataList = new ArrayList<RowData>();
			for (final Object o : synchOutput.getCollectedElements()) {
				if (o instanceof Schedule) {
					final RowData rd = createRowData((Schedule) o, synchOutput.getScenarioInstance(o));
					rowDataList.add(rd);
					if (synchOutput.isPinned(o)) {
						pinnedData = rd;
					}
				}
			}
			rowData = rowDataList.toArray(rowData);
		}

		if (rowData.length == 0) {
			rowData = new RowData[] { new RowData("", null, null, null) };
		}

	}

	private RowData pinnedData = new RowData("", null, null, null);

	public RowData getPinnedData() {
		return pinnedData;
	}

	@Override
	public void dispose() {

	}
}
