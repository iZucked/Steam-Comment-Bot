/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.license.features.KnownFeatures;
import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.LocalDateAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

public class InventoryCapacityPane extends ScenarioTableViewerPane {

	public InventoryCapacityPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addTypicalColumn("Date", new LocalDateAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_Date(), getCommandHandler()));
		addTypicalColumn("Min (m³)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MinVolume(), getCommandHandler()));
		addTypicalColumn("Max (m³)", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MaxVolume(), getCommandHandler()));
		if (LicenseFeatures.isPermitted(KnownFeatures.FEATURE_INVENTORY_CV_MODEL)) {
			addTypicalColumn("Min CV", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MinCV(), getCommandHandler()));
			addTypicalColumn("Max CV", new NumericAttributeManipulator(CargoPackage.eINSTANCE.getInventoryCapacityRow_MaxCV(), getCommandHandler()));
		}

		setTitle("Capacity");
	}
}
