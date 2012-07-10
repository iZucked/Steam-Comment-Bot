/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
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
	private CharterMarketPane charter;
	private CanalCostsPane route;
	private CooldownCostsPane cool;
	private PortCostPane port;

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addMarketPage(parent);
		addCostsPage(parent);
	}

	private void addCostsPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);
		final SashForm sash3 = new SashForm(sash, SWT.VERTICAL);
		
		route = new CanalCostsPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		route.createControl(sash2);
		route.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_RouteCosts() }), editorPart.getAdapterFactory());
		route.getViewer().setInput(modelObject);

		port = new PortCostPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		port.createControl(sash3);
		port.init(Arrays.asList(new EReference[] {PricingPackage.eINSTANCE.getPricingModel_PortCosts()}), editorPart.getAdapterFactory());
		port.getViewer().setInput(modelObject);
		
		cool = new CooldownCostsPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		cool.createControl(sash3);
		cool.init(Arrays.asList(new EReference[] {PricingPackage.eINSTANCE.getPricingModel_CooldownPrices()}), editorPart.getAdapterFactory());
		cool.getViewer().setInput(modelObject);
		
		editorPart.setPageText(editorPart.addPage(sash), "Costs");
	}

	private void addMarketPage(final Composite parent) {

	}

	private void addIndexPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);
		final SashForm sash3 = new SashForm(sash, SWT.VERTICAL);

		final IndexPane pane = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		pane.createControl(sash2);
		pane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()), editorPart.getAdapterFactory());
		pane.getViewer().setInput(modelObject);
		pane.defaultSetTitle("Commodity Indices");

		final IndexPane pane2 = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		pane2.createControl(sash2);
		pane2.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CharterIndices()), editorPart.getAdapterFactory());
		pane2.getViewer().setInput(modelObject);
		pane2.defaultSetTitle("Chartering Indices");
		pane2.setUseIntegers(true);

		charter = new CharterMarketPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		charter.createControl(sash3);
		charter.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_FleetCost(), PricingPackage.eINSTANCE.getFleetCostModel_CharterCosts() }),
				editorPart.getAdapterFactory());
		charter.getViewer().setInput(modelObject);

		int page = editorPart.addPage(sash);
		editorPart.setPageText(page, "Markets");
	}

	@Override
	public void setLocked(boolean locked) {
		route.setLocked(locked);
		charter.setLocked(locked);
		cool.setLocked(locked);
		port.setLocked(locked);
	}
}
