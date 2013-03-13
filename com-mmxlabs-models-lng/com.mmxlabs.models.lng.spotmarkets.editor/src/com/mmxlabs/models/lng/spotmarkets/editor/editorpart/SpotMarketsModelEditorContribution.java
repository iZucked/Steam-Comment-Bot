/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.editor.editorpart;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.spotmarkets.CharterCostModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SpotMarketsModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private CharterMarketPane fleetCostPane;

	private int indexPage = -1;
	private int spotCargoMarketsPage = -1;

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addSpotCargoMarketPage(parent);
	}

	private void addIndexPage(final Composite parent) {

		fleetCostPane = new CharterMarketPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		fleetCostPane.createControl(parent);
		fleetCostPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharteringSpotMarkets() }), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		fleetCostPane.getViewer().setInput(modelObject);

		indexPage = editorPart.addPage(fleetCostPane.getControl());
		editorPart.setPageText(indexPage, "Charter Markets");
	}

	private void addSpotCargoMarketPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		// final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);
		// final SashForm sash3 = new SashForm(sash, SWT.VERTICAL);

		// final SpotMarketGroupPane desPurchasePane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(),
		// SpotType.DES_PURCHASE);
		// desPurchasePane.createControl(sash2);
		// desPurchasePane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsMode_DesPurchaseSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
		// editorPart.getAdapterFactory());
		// desPurchasePane.getViewer().setInput(modelObject);
		// desPurchasePane.defaultSetTitle("DES Purchases");

		final SpotMarketGroupPane desSalesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.DES_SALE);
		desSalesPane.createControl(sash);
		desSalesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesSalesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		desSalesPane.getViewer().setInput(modelObject);
		desSalesPane.defaultSetTitle("DES Sales");
		//
		// final SpotMarketGroupPane fobSalesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.FOB_SALE);
		// fobSalesPane.createControl(sash3);
		// fobSalesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsMode_FobSalesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
		// editorPart.getAdapterFactory());
		// fobSalesPane.getViewer().setInput(modelObject);
		// fobSalesPane.defaultSetTitle("FOB Sales");

		final SpotMarketGroupPane fobPurchasesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.FOB_PURCHASE);
		fobPurchasesPane.createControl(sash);
		fobPurchasesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobPurchasesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		fobPurchasesPane.getViewer().setInput(modelObject);
		fobPurchasesPane.defaultSetTitle("FOB Purchases");

		spotCargoMarketsPage = editorPart.addPage(sash);
		editorPart.setPageText(spotCargoMarketsPage, "Spot Cargoes");
	}

	@Override
	public void setLocked(final boolean locked) {
		fleetCostPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final Object target = dcsd.getTarget();
			if (target instanceof CharterCostModel) {
				return true;
			}
			if (target instanceof SpotMarketGroup || target instanceof SpotMarket) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final Object target = dcsd.getTarget();
			if (target instanceof CharterCostModel) {
				editorPart.setActivePage(indexPage);
				fleetCostPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				return;
			}
			if (target instanceof SpotMarketGroup || target instanceof SpotMarket) {
				editorPart.setActivePage(spotCargoMarketsPage);

				// fleetCostPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				return;
			}
		}
	}
}
