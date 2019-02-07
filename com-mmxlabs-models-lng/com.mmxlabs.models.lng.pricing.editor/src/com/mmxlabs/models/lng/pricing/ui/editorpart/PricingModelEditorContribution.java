/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PricingModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private IndexPane indexPane;
//	private SettledPricesPane settledPricesPane;

	private int indexPage = -1;
//	private int settledPricesPage = -1;

	@Override
	public void addPages(final Composite parent) {
		{
			indexPane = new IndexPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			indexPane.createControl(parent);
			indexPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_BunkerFuelCurves()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			indexPane.setInput(modelObject);

			indexPage = editorPart.addPage(indexPane.getControl());
			editorPart.setPageText(indexPage, "Curves");
		}
//		if (LicenseFeatures.isPermitted("features:paperdeals")) {
//			settledPricesPane = new SettledPricesPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
//			settledPricesPane.createControl(parent);
//			settledPricesPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_SettledPrices()), editorPart.getAdapterFactory(), editorPart.getModelReference());
//			settledPricesPane.setInput(modelObject);
//
//			settledPricesPage = editorPart.addPage(settledPricesPane.getControl());
//			editorPart.setPageText(settledPricesPage, "Settled Prices");
//		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(indexPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Markets");
	}

	@Override
	public void setLocked(final boolean locked) {
		if (indexPane != null) {
			indexPane.setLocked(locked);
		}
//		if (settledPricesPane != null) {
//			settledPricesPane.setLocked(locked);
//		}
	}

	@Override
	public boolean canHandle(final IStatus status) {
		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;

			final EObject target = dcsd.getTarget();
			if (target instanceof CommodityCurve || target instanceof CharterCurve || target instanceof BunkerFuelCurve || target instanceof CurrencyCurve) {
				return true;
			}
			if (target instanceof YearMonthPoint) {
				return true;
			}
			if (target instanceof IndexPoint) {
				return true;
			}
			if (target instanceof PricingModel) {
				return true;
			}
			if (target instanceof UnitConversion) {
				return true;
			}
			if (target instanceof PricingModel) {
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
			if (target instanceof YearMonthPoint) {
				target = ((YearMonthPoint) target).eContainer();
			}

			if (target instanceof PricingModel) {
				if (indexPane != null) {
					editorPart.setActivePage(indexPage);
				}
				return;
			}
			if (target instanceof CommodityCurve || target instanceof CharterCurve || target instanceof BunkerFuelCurve || target instanceof CurrencyCurve) {

				if (indexPane != null) {
					editorPart.setActivePage(indexPage);
					indexPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				}
				return;
			}
			if (target instanceof UnitConversion) {
				if (indexPane != null) {
					editorPart.setActivePage(indexPage);
					indexPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
					indexPane.openUnitsEditor();
				}
				return;
			}
		}
	}
}
