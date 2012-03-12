/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Arrays;
import java.util.Collections;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.ui.editorpart.IJointModelEditorContribution;
import com.mmxlabs.models.ui.editorpart.JointModelEditorPart;

public class PricingModelEditorContribution implements IJointModelEditorContribution {
	private UUIDObject modelObject;
	private JointModelEditorPart editorPart;

	@Override
	public void init(JointModelEditorPart editorPart, MMXRootObject rootObject, UUIDObject modelObject) {
		this.editorPart = editorPart;
		this.modelObject = modelObject;
	}

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addMarketPage(parent);
		addPricingPage(parent);
	}

	private void addPricingPage(final Composite parent) {
		// pricing items are:
		// 1. base fuel prices
		// this is a table with 1 row per base fuel, letting you pick a commodity index
		// 2. canal prices
		// not sure; hierarchy perhaps?
		// 3. charter prices
		// table per vessel class? or just a normal table?

		// these are all special cases - for now could use standard parts.
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);

		final BaseFuelPricingPane base = new BaseFuelPricingPane(editorPart.getSite().getPage(), editorPart);
		base.createControl(sash);
		base.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_FleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_BaseFuelPrices() }), editorPart.getAdapterFactory());

		base.getViewer().setInput(modelObject);

		final CharterPricingPane charter = new CharterPricingPane(editorPart.getSite().getPage(), editorPart);
		charter.createControl(sash);
		charter.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_FleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_CharterCosts() }),
				editorPart.getAdapterFactory());
		charter.getViewer().setInput(modelObject);

		final VesselRoutePricingPane route = new VesselRoutePricingPane(editorPart.getSite().getPage(), editorPart);
		route.createControl(sash);
		route.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_RouteCosts() }), editorPart.getAdapterFactory());
		route.getViewer().setInput(modelObject);

		int page = editorPart.addPage(sash);
		editorPart.setPageText(page, "Pricing");
	}

	private void addMarketPage(final Composite parent) {

	}

	private void addIndexPage(final Composite parent) {
		final IndexEditorPane pane = new IndexEditorPane(editorPart.getSite().getPage(), editorPart);
		pane.createControl(parent);
		pane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()), editorPart.getAdapterFactory());
		pane.getViewer().setInput(modelObject);

		int page = editorPart.addPage(pane.getControl());
		editorPart.setPageText(page, "Indices");
	}

}
