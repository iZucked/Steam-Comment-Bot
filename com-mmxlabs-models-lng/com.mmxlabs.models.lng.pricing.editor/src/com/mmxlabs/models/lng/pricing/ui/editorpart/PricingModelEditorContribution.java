/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PricingModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
//	private CanalCostsPane route;
//	private CooldownCostsPane cool;
//	private PortCostPane port;
	private IndexPane charterIndicesPane;
	private IndexPane commodityPane;

	private int indexPage = -1;
//	private int costsPage = -1;

	@Override
	public void addPages(final Composite parent) {
		addIndexPage(parent);
//		addCostsPage(parent);
	}

//	private void addCostsPage(final Composite parent) {
//		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
//		final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);
//		final SashForm sash3 = new SashForm(sash, SWT.VERTICAL);
//
//		route = new CanalCostsPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
//		route.createControl(sash2);
//		route.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_RouteCosts() }), editorPart.getAdapterFactory());
//		route.getViewer().setInput(modelObject);
//
//		port = new PortCostPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
//		port.createControl(sash3);
//		port.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_PortCosts() }), editorPart.getAdapterFactory());
//		port.getViewer().setInput(modelObject);
//
//		cool = new CooldownCostsPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
//		cool.createControl(sash3);
//		cool.init(Arrays.asList(new EReference[] { PricingPackage.eINSTANCE.getPricingModel_CooldownPrices() }), editorPart.getAdapterFactory());
//		cool.getViewer().setInput(modelObject);
//
//		costsPage = editorPart.addPage(sash);
//		editorPart.setPageText(costsPage, "Costs");
//	}

	private void addIndexPage(final Composite parent) {
		final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
		final SashForm sash2 = new SashForm(sash, SWT.VERTICAL);

		commodityPane = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		commodityPane.createControl(sash2);
		commodityPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		commodityPane.getViewer().setInput(modelObject);
		commodityPane.defaultSetTitle("Commodity Indices");

		charterIndicesPane = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		charterIndicesPane.setUseIntegers(true);
		charterIndicesPane.createControl(sash2);
		charterIndicesPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_CharterIndices()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		charterIndicesPane.getViewer().setInput(modelObject);
		charterIndicesPane.defaultSetTitle("Chartering Indices");

		indexPage = editorPart.addPage(sash);
		editorPart.setPageText(indexPage, "Markets");
	}


	@Override
	public void setLocked(final boolean locked) {
//		route.setLocked(locked);
//		cool.setLocked(locked);
//		port.setLocked(locked);
		commodityPane.setLocked(locked);
		charterIndicesPane.setLocked(locked);
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();
			if (target instanceof Index) {
				final EStructuralFeature containingFeature = target.eContainingFeature();
				return (containingFeature == PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES || containingFeature == PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);
			}
			if (target instanceof IndexPoint) {
				return true;
			}
			if (target instanceof FleetCostModel) {
				return true;
			}
//			if (target instanceof RouteCost) {
//				return true;
//			}
//			if (target instanceof PortCost) {
//				return true;
//			}
//			if (target instanceof CooldownPrice) {
//				return true;
//			}
		}
		return false;
	}

	@Override
	public void handle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			Object target = dcsd.getTarget();
			// Be careful of Index as it can belong to various containers...
			if (target instanceof IndexPoint<?>) {
				target = ((IndexPoint<?>) target).eContainer();
			}

			if (target instanceof Index<?>) {
				final Index<?> index = (Index<?>) target;
				// get container reference
				
				final EStructuralFeature containingFeature = index.eContainingFeature();

				if (containingFeature == PricingPackage.eINSTANCE.getPricingModel_CharterIndices()) {
					editorPart.setActivePage(indexPage);
					charterIndicesPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
					return;
				}
				if (containingFeature == PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()) {
					editorPart.setActivePage(indexPage);
					commodityPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
					return;
				}
			}
//			if (target instanceof CooldownPrice) {
//				editorPart.setActivePage(costsPage);
//				cool.getScenarioViewer().setSelection(new StructuredSelection(target), true);
//				return;
//			}
//			if (target instanceof RouteCost) {
//				editorPart.setActivePage(costsPage);
//				route.getScenarioViewer().setSelection(new StructuredSelection(target), true);
//				return;
//			}
//			if (target instanceof PortCost) {
//				editorPart.setActivePage(costsPage);
//				port.getScenarioViewer().setSelection(new StructuredSelection(target), true);
//				return;
//			}
		}
	}
}
