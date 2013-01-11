/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

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

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addTypicalColumn("Ports", new MultipleReferenceManipulator(PricingPackage.eINSTANCE.getCooldownPrice_Ports(), getReferenceValueProviderCache(), getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Index", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getCooldownPrice_Index(), getReferenceValueProviderCache(), getEditingDomain()));
		defaultSetTitle("Cooldown Costs");
	}
}
