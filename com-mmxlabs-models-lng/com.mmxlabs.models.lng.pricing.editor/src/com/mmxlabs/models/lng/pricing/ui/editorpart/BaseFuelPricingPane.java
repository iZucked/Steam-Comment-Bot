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
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * Quick, untidy pane for editing base fuel pricing
 * 
 * @author hinton
 *
 */
public class BaseFuelPricingPane extends ScenarioTableViewerPane {
	public BaseFuelPricingPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	/* (non-Javadoc)
	 * @see com.mmxlabs.models.lng.types.ui.tabular.ScenarioTableViewerPane#init(java.util.List, org.eclipse.emf.common.notify.AdapterFactory)
	 */
	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		addTypicalColumn("Base Fuel", 
				new SingleReferenceManipulator(PricingPackage.eINSTANCE.getBaseFuelCost_Fuel(), 
						getReferenceValueProviderCache(), getEditingDomain()));
		
		addTypicalColumn("Commodity Index", 
				new SingleReferenceManipulator(PricingPackage.eINSTANCE.getBaseFuelCost_Price(), 
						getReferenceValueProviderCache(), getEditingDomain()));
		
		defaultSetTitle("Base Fuel Pricing");
	}
}
