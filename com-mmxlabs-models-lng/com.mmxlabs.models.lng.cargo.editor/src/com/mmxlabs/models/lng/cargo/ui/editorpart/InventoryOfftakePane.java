/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.ui.manipulators.InventoryPeriodEnumAttributeManipulator;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class InventoryOfftakePane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public InventoryOfftakePane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		
		addTypicalColumn("Start", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_StartDate(), editingDomain));
		addTypicalColumn("End", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_EndDate(), editingDomain));
		addTypicalColumn("Volume (m³)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Volume(), editingDomain));
		addTypicalColumn("Counterparty", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_CounterParty(), editingDomain));
		addTypicalColumn("Frequency", new InventoryPeriodEnumAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Period(), editingDomain));
		addTypicalColumn("Reliability (%)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_Reliability(), editingDomain));
		addTypicalColumn("Forecast", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryEventRow_ForecastDate(), editingDomain));

		setTitle("Out");
	}
}
