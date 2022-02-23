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

import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.ui.editorpart.CanalCostsPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.util.ScenarioModelUtil;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class CanalCostsView extends ScenarioTableViewerView<CanalCostsPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.CanalCostsView";

	@Override
	protected CanalCostsPane createViewerPane() {
		return new CanalCostsPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final CanalCostsPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			MMXRootObject rootObject = getRootObject();
			CostModel costModel = ScenarioModelUtil.getCostModel((LNGScenarioModel) rootObject);
			if (!costModel.getRouteCosts().isEmpty()) {
				pane.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getCostModel_RouteCosts() }), getAdapterFactory(), getModelReference());
			}
			pane.setInput(costModel, costModel.getSuezCanalTariff(), costModel.getPanamaCanalTariff());
		}
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator dcsd) {
			final Object target = dcsd.getTarget();

			if (target instanceof RouteCost) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}
