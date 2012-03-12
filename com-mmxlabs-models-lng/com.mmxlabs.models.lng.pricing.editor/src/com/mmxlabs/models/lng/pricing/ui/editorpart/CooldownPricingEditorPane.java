/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * @author hinton
 *
 */
public class CooldownPricingEditorPane extends ScenarioTableViewerPane {
	/**
	 * @param page
	 * @param part
	 */
	public CooldownPricingEditorPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addTypicalColumn("Ports", new MultipleReferenceManipulator(PricingPackage.eINSTANCE.getCooldownPrice_Ports(), getReferenceValueProviderCache(), getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		addTypicalColumn("Index", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getCooldownPrice_Index(), getReferenceValueProviderCache(), getEditingDomain()));
		defaultSetTitle("Cooldown Prices");
	}
}
