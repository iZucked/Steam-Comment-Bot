/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * Charter price editor
 * 
 * @author hinton
 * 
 */
public class CharterOutMarketPane extends ScenarioTableViewerPane {
	
	private CharterOutParametersToolbarEditor charterOutParametersToolbarEditor;
	
	public CharterOutMarketPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		
		final ToolBarManager toolbar = getToolBarManager();
		setMinToolbarHeight(30);
		
		charterOutParametersToolbarEditor = new CharterOutParametersToolbarEditor("charter_out_parameters_toolbar", //
				scenarioEditingLocation.getEditingDomain(), (LNGScenarioModel)scenarioEditingLocation.getRootObject());
		
		toolbar.appendToGroup(EDIT_GROUP, charterOutParametersToolbarEditor);
		toolbar.update(true);

		addTypicalColumn("Name ", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));

		addTypicalColumn("Active", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getEditingDomain()));

		addTypicalColumn("Vessels", new MultipleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_Vessels(), getReferenceValueProviderCache(), getEditingDomain(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Min duration", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_MinCharterOutDuration(), getEditingDomain()));

		addTypicalColumn("Lending rate", new BasicAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_CharterOutRate(), getEditingDomain()));

		defaultSetTitle("Charter out");
	}
}
