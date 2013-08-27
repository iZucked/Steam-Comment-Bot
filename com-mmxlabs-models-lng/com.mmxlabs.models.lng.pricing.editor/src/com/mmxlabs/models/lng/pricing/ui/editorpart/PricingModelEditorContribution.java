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
import org.eclipse.swt.widgets.Composite;

import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PricingModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private IndexPane indexPane;

	private int indexPage = -1;

	@Override
	public void addPages(final Composite parent) {

		indexPane = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
		indexPane.createControl(parent);
		indexPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_BaseFuelPrices()), editorPart.getAdapterFactory(), editorPart.getEditingDomain().getCommandStack());
		indexPane.setInput(modelObject);
		indexPane.defaultSetTitle("Base Fuel Indices");

		indexPage = editorPart.addPage(indexPane.getControl());
		editorPart.setPageText(indexPage, "Markets");
	}

	@Override
	public void setLocked(final boolean locked) {
		if (indexPane != null) {
			indexPane.setLocked(locked);
		}
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();
			if (target instanceof Index) {
				final EStructuralFeature containingFeature = target.eContainingFeature();
				return (containingFeature == PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES || containingFeature == PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES || containingFeature == PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES);
			}
			if (target instanceof IndexPoint) {
				return true;
			}
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

				if (containingFeature == PricingPackage.eINSTANCE.getPricingModel_CharterIndices() || containingFeature == PricingPackage.eINSTANCE.getPricingModel_CommodityIndices()
						|| containingFeature == PricingPackage.eINSTANCE.getPricingModel_BaseFuelPrices()) {
					if (indexPane != null) {
						editorPart.setActivePage(indexPage);
						indexPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
					}
					return;
				}
			}
		}
	}
}
