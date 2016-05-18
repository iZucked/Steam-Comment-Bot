/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import com.mmxlabs.models.lng.spotmarkets.CharterOutMarket;
import com.mmxlabs.models.lng.spotmarkets.DESPurchaseMarket;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class SpotMarketsModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private CharterInMarketPane charterInMarketPane;
	private CharterOutMarketPane charterOutMarketPane;

	private int indexPage = -1;
	private int spotCargoMarketsPage = -1;
	private SpotMarketGroupPane fobPurchasesPane;
	private SpotMarketGroupPane desPurchasePane;
	private SpotMarketGroupPane desSalesPane;
	private SpotMarketGroupPane fobSalesPane;

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
		addSpotCargoMarketPage(parent);
	}

	private void addIndexPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.VERTICAL);

		charterInMarketPane = new CharterInMarketPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		charterInMarketPane.createControl(sash);
		charterInMarketPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterInMarkets() }), editorPart.getAdapterFactory(),
				editorPart.getEditingDomain().getCommandStack());
		charterInMarketPane.getViewer().setInput(modelObject);

		charterOutMarketPane = new CharterOutMarketPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		charterOutMarketPane.createControl(sash);
		charterOutMarketPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_CharterOutMarkets() }), editorPart.getAdapterFactory(),
				editorPart.getEditingDomain().getCommandStack());
		charterOutMarketPane.getViewer().setInput(modelObject);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(charterInMarketPane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCharters");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(charterOutMarketPane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCharters");

		indexPage = editorPart.addPage(sash);
		editorPart.setPageText(indexPage, "Charters");
	}

	private void addSpotCargoMarketPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);
		final SashForm sash3 = new SashForm(sash, SWT.VERTICAL);

		fobPurchasesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.FOB_PURCHASE);
		fobPurchasesPane.createControl(sash2);
		fobPurchasesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobPurchasesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		fobPurchasesPane.getViewer().setInput(modelObject);
		fobPurchasesPane.defaultSetTitle("FOB Purchases");

		desPurchasePane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.DES_PURCHASE);
		desPurchasePane.createControl(sash2);
		desPurchasePane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesPurchaseSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		desPurchasePane.getViewer().setInput(modelObject);
		desPurchasePane.defaultSetTitle("DES Purchases");

		desSalesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.DES_SALE);
		desSalesPane.createControl(sash3);
		desSalesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesSalesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		desSalesPane.getViewer().setInput(modelObject);
		desSalesPane.defaultSetTitle("DES Sales");
		fobSalesPane = new SpotMarketGroupPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars(), SpotType.FOB_SALE);
		fobSalesPane.createControl(sash3);
		fobSalesPane.init(Arrays.asList(new EReference[] { SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobSalesSpotMarket(), SpotMarketsPackage.eINSTANCE.getSpotMarketGroup_Markets() }),
				editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		fobSalesPane.getViewer().setInput(modelObject);
		fobSalesPane.defaultSetTitle("FOB Sales");

		spotCargoMarketsPage = editorPart.addPage(sash);
		editorPart.setPageText(spotCargoMarketsPage, "Spot Cargoes");

		PlatformUI.getWorkbench().getHelpSystem().setHelp(fobPurchasesPane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCargoes");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(desPurchasePane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCargoes");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(desSalesPane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCargoes");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(fobSalesPane.getControl(), "com.mmxlabs.lingo.doc.Editor_SpotCargoes");
	}

	@Override
	public void setLocked(final boolean locked) {
		charterInMarketPane.setLocked(locked);
		charterOutMarketPane.setLocked(locked);
		fobPurchasesPane.setLocked(locked);
		desPurchasePane.setLocked(locked);
		desSalesPane.setLocked(locked);
		fobSalesPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final Object target = dcsd.getTarget();

			if (target instanceof SpotMarketsModel) {
				final SpotMarketsModel spotMarketsModel = (SpotMarketsModel) target;
				if (dcsd.getFeaturesForEObject(spotMarketsModel).contains(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DEFAULT_NOMINAL_MARKET)) {
					return true;
				}
			}
			if (target instanceof CharterInMarket) {
				return true;
			}
			if (target instanceof CharterOutMarket) {
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
			if (target instanceof SpotMarketsModel) {
				final SpotMarketsModel spotMarketsModel = (SpotMarketsModel) target;
				if (dcsd.getFeaturesForEObject(spotMarketsModel).contains(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DEFAULT_NOMINAL_MARKET)) {
					editorPart.setActivePage(indexPage);
					return;
				}
			}
			if (target instanceof CharterInMarket) {
				editorPart.setActivePage(indexPage);
				charterInMarketPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				return;
			}
			if (target instanceof CharterOutMarket) {
				editorPart.setActivePage(indexPage);
				charterOutMarketPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				return;
			}
			if (target instanceof SpotMarketGroup || target instanceof SpotMarket) {
				editorPart.setActivePage(spotCargoMarketsPage);
				if (target instanceof DESPurchaseMarket) {
					desPurchasePane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				} else if (target instanceof DESSalesMarket) {
					desSalesPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				} else if (target instanceof FOBPurchasesMarket) {
					fobPurchasesPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				} else if (target instanceof FOBSalesMarket) {
					fobSalesPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				}
				return;
			}
		}
	}
}
