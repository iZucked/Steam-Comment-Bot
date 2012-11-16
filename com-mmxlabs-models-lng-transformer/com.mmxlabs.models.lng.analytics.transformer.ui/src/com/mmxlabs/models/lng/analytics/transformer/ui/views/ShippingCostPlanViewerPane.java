/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.transformer.ui.views;

import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * A viewer pane for editing unit cost matrix definitions
 * 
 * @author hinton
 * 
 */
public class ShippingCostPlanViewerPane extends ScenarioTableViewerPane {
	private static final Logger log = LoggerFactory.getLogger(ShippingCostPlanViewerPane.class);

	public ShippingCostPlanViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
		super(page, part, location, actionBars);
	}

	protected boolean showSelectionColumn() {
		return false;
	}

	protected boolean showEvaluateAction() {
		return true;
	}

	@Override
	public void init(final List<EReference> path, final AdapterFactory adapterFactory) {
		super.init(path, adapterFactory);
		addNameManipulator("Name");
		addTypicalColumn("Vessel", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getShippingCostPlan_Vessel(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Hire Rate", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostPlan_NotionalDayRate(), getEditingDomain()));
		addTypicalColumn("Base Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostPlan_BaseFuelPrice(), getEditingDomain()));
	}
}
