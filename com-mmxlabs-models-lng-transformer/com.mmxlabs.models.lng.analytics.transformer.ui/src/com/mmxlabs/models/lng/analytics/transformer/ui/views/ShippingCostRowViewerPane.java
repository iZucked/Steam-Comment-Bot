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

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.ui.dates.DateAttributeManipulator;
import com.mmxlabs.models.ui.editorpart.IScenarioEditingLocation;
import com.mmxlabs.models.ui.tabular.EnumAttributeManipulator;
import com.mmxlabs.models.ui.tabular.NumericAttributeManipulator;
import com.mmxlabs.models.ui.tabular.SingleReferenceManipulator;

/**
 * A viewer pane for editing unit cost matrix definitions
 * 
 * @author hinton
 * 
 */
public class ShippingCostRowViewerPane extends ScenarioTableViewerPane {

	public ShippingCostRowViewerPane(final IWorkbenchPage page, final IWorkbenchPart part, final IScenarioEditingLocation location, final IActionBars actionBars) {
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
		addTypicalColumn("Port", new SingleReferenceManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_Port(), getReferenceValueProviderCache(), getEditingDomain()));
		addTypicalColumn("Date", new DateAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_Date(), getEditingDomain()));
		addTypicalColumn("Gas Price", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CargoPrice(), getEditingDomain()));
		addTypicalColumn("Gas CV", new NumericAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_CvValue(), getEditingDomain()));
		addTypicalColumn("Type", new EnumAttributeManipulator(AnalyticsPackage.eINSTANCE.getShippingCostRow_DestinationType(), getEditingDomain()));

		// Disable sorting
		getScenarioViewer().setComparator(null);
	}
}
