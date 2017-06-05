/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;

public class InventoryCapacityPane extends ScenarioTableViewerPane {

	private final IScenarioEditingLocation jointModelEditor;

	public InventoryCapacityPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
		this.jointModelEditor = location;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		final EditingDomain editingDomain = jointModelEditor.getEditingDomain();
		addTypicalColumn("Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_Date(), editingDomain));
		addTypicalColumn("Min", new BasicAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MinVolume(), editingDomain));
		addTypicalColumn("Max", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MaxVolume(), editingDomain));

		setTitle("Capacity");
	}
}
