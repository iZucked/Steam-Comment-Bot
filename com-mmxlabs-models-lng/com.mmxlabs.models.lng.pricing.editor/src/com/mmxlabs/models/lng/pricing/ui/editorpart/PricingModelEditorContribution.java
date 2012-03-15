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

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;

public class PricingModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private BaseFuelPricingPane base;
	private CharterPricingPane charter;
	private VesselRoutePricingPane route;
	private CooldownPricingEditorPane cool;

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addMarketPage(parent);
		addPricingPage(parent);
	}

	private void addPricingPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);
		final SashForm sash2 = new SashForm(sash, SWT.HORIZONTAL);
		final SashForm sash3 = new SashForm(sash, SWT.HORIZONTAL);
		
		base = new BaseFuelPricingPane(editorPart.getSite().getPage(), editorPart);
		base.createControl(sash2);
		base.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_FleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_BaseFuelPrices() }), editorPart.getAdapterFactory());

		base.getViewer().setInput(modelObject);

		charter = new CharterPricingPane(editorPart.getSite().getPage(), editorPart);
		charter.createControl(sash3);
		charter.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_FleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_CharterCosts() }),
				editorPart.getAdapterFactory());
		charter.getViewer().setInput(modelObject);

		route = new VesselRoutePricingPane(editorPart.getSite().getPage(), editorPart);
		route.createControl(sash3);
		route.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_RouteCosts() }), editorPart.getAdapterFactory());
		route.getViewer().setInput(modelObject);

		cool = new CooldownPricingEditorPane(editorPart.getSite().getPage(), editorPart);
		cool.createControl(sash2);
		cool.init(Arrays.asList(new EReference[] {PricingPackage.eINSTANCE.getPricingModel_CooldownPrices()}), editorPart.getAdapterFactory());
		cool.getViewer().setInput(modelObject);
		
		int page = editorPart.addPage(sash);
		editorPart.setPageText(page, "Pricing");
	}

	private void addMarketPage(final Composite parent) {

	}

	private void addIndexPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		final IndexEditorPane pane = new IndexEditorPane(editorPart.getSite().getPage(), editorPart);
		pane.createControl(sash);
		pane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()), editorPart.getAdapterFactory());
		pane.getViewer().setInput(modelObject);

		final IndexEditorPane pane2 = new IndexEditorPane(editorPart.getSite().getPage(), editorPart);
		pane2.createControl(sash);
		pane2.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CharterIndices()), editorPart.getAdapterFactory());
		pane2.getViewer().setInput(modelObject);
		pane.defaultSetTitle("Commodity Indices");
		pane2.defaultSetTitle("Chartering Indices");
		
		int page = editorPart.addPage(sash);
		editorPart.setPageText(page, "Indices");
	}

	@Override
	public void setLocked(boolean locked) {
		base.setLocked(locked);
		route.setLocked(locked);
		charter.setLocked(locked);
		cool.setLocked(locked);
	}
}
