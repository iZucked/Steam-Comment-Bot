/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarketParameters;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialogUtil;
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
	public CharterOutMarketPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);

		addTypicalColumn("Name ", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));

		addTypicalColumn("Active", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getEditingDomain()));

		addTypicalColumn("Vessels", new MultipleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_Vessels(), getReferenceValueProviderCache(), getEditingDomain(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));

		addTypicalColumn("Min Duration", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_MinCharterOutDuration(), getEditingDomain()));

		addTypicalColumn("Lending Rate", new BasicAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_CharterOutRate(), getEditingDomain()));

		defaultSetTitle("Charter Out  Market");

		final Action editCharterOutStartDateAction = new Action("Edit charter out dates") {
			@Override
			public void run() {

				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);

//					CharterOutStartDate charterOutStartDate = spotMarketsModel.getCharterOutStartDate();
//					if (charterOutStartDate == null) {
//						charterOutStartDate = SpotMarketsFactory.eINSTANCE.createCharterOutStartDate();
//						final Command cmd = SetCommand.create(getEditingDomain(), spotMarketsModel, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE, charterOutStartDate);
//						getEditingDomain().getCommandStack().execute(cmd);
//					}
//
//					DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, charterOutStartDate);
					CharterOutMarketParameters charterOutMarketParameters = spotMarketsModel.getCharterOutMarketParameters();
					if (charterOutMarketParameters == null) {
						charterOutMarketParameters = SpotMarketsFactory.eINSTANCE.createCharterOutMarketParameters();
						final Command cmd = SetCommand.create(getEditingDomain(), spotMarketsModel, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKET_PARAMETERS, charterOutMarketParameters);
						getEditingDomain().getCommandStack().execute(cmd);
					}

					DetailCompositeDialogUtil.editSingleObject(scenarioEditingLocation, charterOutMarketParameters);

				}
			}
		};
		getMenuManager().add(editCharterOutStartDateAction);
		getMenuManager().update(true);
	}
}
