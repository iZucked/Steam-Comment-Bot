/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.manipulators.BasicAttributeManipulator;
import com.mmxlabs.models.ui.tabular.manipulators.MultipleReferenceManipulator;
import com.mmxlabs.scenario.service.model.manager.ModelReference;

/**
 * @author hinton
 * 
 */
public class CooldownCostsPane extends ScenarioTableViewerPane {
	/**
	 * @param page
	 * @param part
	 */
	public CooldownCostsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory, final ModelReference modelReference) {
		super.init(path, adapterFactory, modelReference);
		addTypicalColumn("Ports", new MultipleReferenceManipulator(PricingPackage.eINSTANCE.getCooldownPriceEntry_Ports(), getReferenceValueProviderCache(), getCommandHandler(),
				MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Lumpsum", new BasicAttributeManipulator(PricingPackage.eINSTANCE.getCooldownPriceEntry_LumpsumExpression(), getCommandHandler()));
		addTypicalColumn("Vol. Expression", new BasicAttributeManipulator(PricingPackage.eINSTANCE.getCooldownPriceEntry_VolumeExpression(), getCommandHandler()));
		defaultSetTitle("Cooldown Costs");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.mmxlabs.lingo.doc.Editor_CooldownCosts");
	}
}
