/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.pricing.DatePoint;
import com.mmxlabs.models.lng.pricing.DatePointContainer;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.ui.editorpart.SettledPricesPane;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SettledPricesView extends ScenarioTableViewerView<SettledPricesPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.SettledPricesView";

	@Override
	protected SettledPricesPane createViewerPane() {
		return new SettledPricesPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final SettledPricesPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_SettledPrices() }), getAdapterFactory(), getModelReference());
			pane.setInput(ScenarioModelUtil.getPricingModel(getScenarioDataProvider()));
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

			if (target instanceof DatePoint || target instanceof DatePointContainer) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}
