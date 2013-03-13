/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * Charter price editor
 * 
 * @author hinton
 * 
 */
public class CharterMarketPane extends ScenarioTableViewerPane {
	public CharterMarketPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addTypicalColumn("Vessel Classes", new MultipleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterCostModel_VesselClasses(), getReferenceValueProviderCache(), getEditingDomain(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Spot Count", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterCostModel_SpotCharterCount(), getEditingDomain()));

		addTypicalColumn("Hiring Index", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterCostModel_CharterInPrice(), getReferenceValueProviderCache(), getEditingDomain()));

		addTypicalColumn("Lending Index", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterCostModel_CharterOutPrice(), getReferenceValueProviderCache(), getEditingDomain()));

		defaultSetTitle("Charter Market");
	}
}
