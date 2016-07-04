/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.BooleanAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.ScenarioLock;

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
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final CommandStack commandStack) {
		super.init(path, adapterFactory, commandStack);

		addTypicalColumn("Name ", new BasicAttributeManipulator(MMXCorePackage.eINSTANCE.getNamedObject_Name(), getEditingDomain()));

		addTypicalColumn("Enabled", new BooleanAttributeManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_Enabled(), getEditingDomain()));

		addTypicalColumn("Vessel Class", new SingleReferenceManipulator(SpotMarketsPackage.eINSTANCE.getSpotCharterMarket_VesselClass(), getReferenceValueProviderCache(), getEditingDomain()));

		addTypicalColumn("Min Duration", new NumericAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_MinCharterOutDuration(), getEditingDomain()));

		addTypicalColumn("Lending Rate", new BasicAttributeManipulator(SpotMarketsPackage.eINSTANCE.getCharterOutMarket_CharterOutRate(), getEditingDomain()));

		defaultSetTitle("Charter Out  Market");

		final Action editCharterOutStartDateAction = new Action("Edit Charter Out Start Date") {
			@Override
			public void run() {

				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					final SpotMarketsModel spotMarketsModel = ScenarioModelUtil.getSpotMarketsModel(scenarioModel);

					CharterOutStartDate charterOutStartDate = spotMarketsModel.getCharterOutStartDate();
					if (charterOutStartDate == null) {
						charterOutStartDate = SpotMarketsFactory.eINSTANCE.createCharterOutStartDate();
						final Command cmd = SetCommand.create(getEditingDomain(), spotMarketsModel, SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE, charterOutStartDate);
						getEditingDomain().getCommandStack().execute(cmd);
					}

					final DetailCompositeDialog editor = new DetailCompositeDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), scenarioEditingLocation.getDefaultCommandHandler());

					final ScenarioLock editorLock = scenarioEditingLocation.getEditorLock();
					try {
						editorLock.claim();
						scenarioEditingLocation.setDisableUpdates(true);
						editor.open(scenarioEditingLocation, scenarioEditingLocation.getRootObject(), Collections.<EObject> singletonList(charterOutStartDate));
					} finally {
						scenarioEditingLocation.setDisableUpdates(false);
						editorLock.release();
					}
				}
			}
		};
		getMenuManager().add(editCharterOutStartDateAction);
		getMenuManager().update(true);
	}
}
