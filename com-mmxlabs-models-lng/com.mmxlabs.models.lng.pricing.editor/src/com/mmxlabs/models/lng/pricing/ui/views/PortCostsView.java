/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.editorpart.PortCostPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PortCostsView extends ScenarioTableViewerView<PortCostPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.PortCostsView";

	@Override
	protected PortCostPane createViewerPane() {
		return new PortCostPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final PortCostPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PricingModel(), PricingPackage.eINSTANCE.getPricingModel_PortCosts() }), getAdapterFactory(),
					domain.getCommandStack());
			pane.getViewer().setInput(getRootObject());
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			final Object target = dcsd.getTarget();

			if (target instanceof PortCost) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}
