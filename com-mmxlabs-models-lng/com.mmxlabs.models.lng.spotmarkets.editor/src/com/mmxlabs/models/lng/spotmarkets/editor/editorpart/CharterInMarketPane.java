/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;

/**
 * Charter price editor
 * 
 * @author hinton
 * 
 */
public class CharterInMarketPane extends ScenarioTableViewerPane {
	public CharterInMarketPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);
		addTypicalColumn("Name ", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));

		addTypicalColumn("Enabled", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getEditingDomain()));

		addTypicalColumn("Vessel Classes", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_VesselClass(), getReferenceValueProviderCache(), getEditingDomain()));

		addTypicalColumn("Count", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterInMarket_SpotCharterCount(), getEditingDomain()));
	}
}