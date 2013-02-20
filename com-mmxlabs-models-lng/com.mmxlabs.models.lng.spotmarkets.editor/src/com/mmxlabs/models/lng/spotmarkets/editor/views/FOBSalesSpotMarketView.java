/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.spotmarkets.editor.editorpart.SpotMarketGroupPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class FOBSalesSpotMarketView extends ScenarioTableViewerView<SpotMarketGroupPane> {
	public static final String ID = "com.mmxlabs.models.lng.pricing.editor.FOBSalesSpotMarketView";

	@Override
	protected SpotMarketGroupPane createViewerPane() {
		return new SpotMarketGroupPane(getSite().getPage(), this, this, getViewSite().getActionBars(), SpotType.FOB_SALE);
	}

	@Override
	protected void initViewerPane(final SpotMarketGroupPane pane) {
		pane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobSalesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }), null);
		pane.getViewer().setInput(getRootObject().getSubModel(SpotMarketsModel.class));
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			Object target = dcsd.getTarget();

			if (target instanceof SpotMarket) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(target), true);
			}
		}
	}
}
