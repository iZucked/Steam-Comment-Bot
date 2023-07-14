/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.time.LocalDate;
import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.nebula.jface.gridviewer.GridViewerColumn;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.event.EventHandler;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.InventoryFacilityType;
import com.mmxlabs.models.lng.cargo.InventoryFeedRow;
import com.mmxlabs.models.lng.cargo.ui.manipulators.InventoryPeriodEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.rcp.common.handlers.TodayHandler;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class InventoryFeedPane extends ScenarioTableViewerPane {

	private EventHandler todayHandler;
	private Inventory selectedInventory;
	private GridViewerColumn cvColumn;

	public InventoryFeedPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Start", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_StartDate(), getCommandHandler()));
		addTypicalColumn("End", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_EndDate(), getCommandHandler()));
		addTypicalColumn("Vol (m³)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Volume(), getCommandHandler()));
		addTypicalColumn("Forecast", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_ForecastDate(), getCommandHandler()));

		addTypicalColumn("Reliability (%)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Reliability(), getCommandHandler()));
		addTypicalColumn("Period", new InventoryPeriodEnumAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Period(), getCommandHandler()));
		addTypicalColumn("Counterparty", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_CounterParty(), getCommandHandler()));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL)) {
			cvColumn = addTypicalColumn("CV", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryFeedRow_Cv(), getCommandHandler()));
		}
		setTitle("In");

		// Adding an event broker for the snap-to-date event todayHandler
		IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
		this.todayHandler = event -> snapTo((LocalDate) event.getProperty(IEventBroker.DATA));
		eventBroker.subscribe(TodayHandler.EVENT_SNAP_TO_DATE, this.todayHandler);

	}

	private void snapTo(LocalDate property) {
		if (scenarioViewer == null) {
			return;
		}
		final Grid grid = scenarioViewer.getGrid();
		if (grid == null) {
			return;
		}
		final int count = grid.getItemCount();
		if (count <= 0) {
			return;
		}

		final GridItem[] items = grid.getItems();
		int pos = -1;
		for (GridItem item : items) {
			Object oData = item.getData();
			if (oData instanceof InventoryFeedRow ier) {
				if (ier.getStartDate().isAfter(property)) {
					break;
				}
				pos++;
			}
		}
		if (pos != -1) {
			grid.deselectAll();
			grid.select(pos);
			grid.showSelection();
		}
	}

	public void setInput(final Inventory input) {
		getViewer().setInput(input);
		if (cvColumn != null) {
			cvColumn.getColumn().setVisible(input.getFacilityType() != InventoryFacilityType.UPSTREAM);
		}
	}

	@Override
	public void dispose() {
		if (this.todayHandler != null) {
			IEventBroker eventBroker = PlatformUI.getWorkbench().getService(IEventBroker.class);
			eventBroker.unsubscribe(this.todayHandler);
		}
		super.dispose();
	}
}
