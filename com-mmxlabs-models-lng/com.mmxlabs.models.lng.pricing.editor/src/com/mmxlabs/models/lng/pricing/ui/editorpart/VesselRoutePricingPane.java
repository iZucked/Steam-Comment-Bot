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
import org.eclipse.ui.IWorkbenchPart;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * Quick hack for vessel route cost editing
 * 
 * @author hinton
 *
 */
public class VesselRoutePricingPane extends ScenarioTableViewerPane {
	public VesselRoutePricingPane(IWorkbenchPage page, IWorkbenchPart part, IScenarioEditingLocation location) {
		super(page, part, location);
	}

	@Override
	public void init(List<EReference> path, AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		
		addTypicalColumn("Route", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getRouteCost_Route(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Vessel Class", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getRouteCost_VesselClass(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.eINSTANCE.getRouteCost_LadenCost(), getEditingDomain()));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.eINSTANCE.getRouteCost_BallastCost(), getEditingDomain()));
		
		defaultSetTitle("Route Pricing");
	}

	@Override
	protected Action createDeleteAction() {
		return null;
	}
}
