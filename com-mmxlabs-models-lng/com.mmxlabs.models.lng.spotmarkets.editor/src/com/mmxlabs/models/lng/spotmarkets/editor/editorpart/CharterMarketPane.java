/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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
import com.mmxlabs.models.lng.spotmarkets.CharterOutStartDate;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.editors.dialogs.DetailCompositeDialog;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.SingleReferenceManipulator;
import com.mmxlabs.scenario.service.model.ScenarioLock;

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

		final Action editCharterOutStartDateAction = new Action("Edit Charter Out Start Date") {
			@Override
			public void run() {

				final MMXRootObject rootObject = scenarioEditingLocation.getRootObject();
				if (rootObject instanceof LNGScenarioModel) {
					final LNGScenarioModel scenarioModel = (LNGScenarioModel) rootObject;
					if (scenarioModel != null) {
						final SpotMarketsModel spotMarketsModel = scenarioModel.getSpotMarketsModel();
						if (spotMarketsModel != null) {

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
				}
			}
		};
		getMenuManager().add(editCharterOutStartDateAction);
		getMenuManager().update(true);
	}
}
