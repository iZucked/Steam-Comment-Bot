/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IActionBars;
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
public class CanalCostsPane extends ScenarioTableViewerPane {
	public CanalCostsPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);

		addTypicalColumn("Route", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getRouteCost_Route(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Vessel Class", new SingleReferenceManipulator(PricingPackage.eINSTANCE.getRouteCost_VesselClass(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Laden Toll", new NumericAttributeManipulator(PricingPackage.eINSTANCE.getRouteCost_LadenCost(), getEditingDomain()));
		addTypicalColumn("Ballast Toll", new NumericAttributeManipulator(PricingPackage.eINSTANCE.getRouteCost_BallastCost(), getEditingDomain()));

		defaultSetTitle("Canal Costs");
	}

	@Override
	protected Action createDeleteAction() {
		return null;
	}
}
