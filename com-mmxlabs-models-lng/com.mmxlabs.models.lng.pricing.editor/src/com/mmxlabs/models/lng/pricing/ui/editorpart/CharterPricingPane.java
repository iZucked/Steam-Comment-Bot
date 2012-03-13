/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;
import com.mmxlabs.models.ui.tabular.MultipleReferenceManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * Charter price editor
 * 
 * @author hinton
 *
 */
public class CharterPricingPane extends ScenarioTableViewerPane {
	public CharterPricingPane(IWorkbenchPage page, JointModelEditorPart part) {
		super(page, part);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Vessel Classes",
				new MultipleReferenceManipulator(PricingPackage.eINSTANCE.getCharterCostModel_VesselClasses(), 
						getReferenceValueProviderCache(), getEditingDomain(), MMXCorePackage.eINSTANCE.getNamedObject_Name()));
		
		addTypicalColumn("Spot Count", 
				new NumericAttributeManipulator(PricingPackage.eINSTANCE.getCharterCostModel_SpotCharterCount(), getEditingDomain()));
		
		addTypicalColumn("Hiring Index", 
				new SingleReferenceManipulator(PricingPackage.eINSTANCE.getCharterCostModel_CharterInPrice(), getReferenceValueProviderCache(), getEditingDomain()));
		
		addTypicalColumn("Lending Index", 
				new SingleReferenceManipulator(PricingPackage.eINSTANCE.getCharterCostModel_CharterOutPrice(), getReferenceValueProviderCache(), getEditingDomain()));
		
		defaultSetTitle("Charter Pricing");
	}
}
