/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.license.features.LicenseFeatures;
import com.mmxlabs.models.lng.pricing.BunkerFuelCurve;
import com.mmxlabs.models.lng.pricing.CharterCurve;
import com.mmxlabs.models.lng.pricing.CommodityCurve;
import com.mmxlabs.models.lng.pricing.CurrencyCurve;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.MarketIndex;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
import com.mmxlabs.models.lng.pricing.YearMonthPoint;
import com.mmxlabs.models.ui.editorpart.BaseJointModelEditorContribution;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class PricingModelEditorContribution extends BaseJointModelEditorContribution<PricingModel> {
	private IndexPane indexPane;
	
	private MarketIndexViewerPane mivPane;
	private SettleStrategyViewerPane ssvPane;

	private int indexPage = -1;
	private int mivpPage = -1;

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
		if (LicenseFeatures.isPermitted("features:paperdeals")) {
			final SashForm sash = new SashForm(parent, SWT.HORIZONTAL);
			mivPane = new MarketIndexViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			ssvPane = new SettleStrategyViewerPane(editorPart.getSite().getPage(), editorPart, editorPart, editorPart.getEditorSite().getActionBars());
			
			mivPane.createControl(sash);
			ssvPane.createControl(sash);
			
			mivPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_MarketIndices()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			ssvPane.init(Collections.singletonList(PricingPackage.eINSTANCE.getPricingModel_SettleStrategies()), editorPart.getAdapterFactory(), editorPart.getModelReference());
			
			mivPane.getViewer().setInput(modelObject);
			ssvPane.getViewer().setInput(modelObject);

			mivpPage = editorPart.addPage(sash);
			
			editorPart.setPageText(mivpPage, "Indices");
		}
		PlatformUI.getWorkbench().getHelpSystem().setHelp(indexPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Markets");
	}

	@Override
	public void setLocked(final boolean locked) {
		if (indexPane != null) {
			indexPane.setLocked(locked);
		}
		if (mivPane != null) {
			mivPane.setLocked(locked);
		}
		if (ssvPane != null) {
			ssvPane.setLocked(locked);
		}
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
			if (target instanceof MarketIndex) {
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
			if (target instanceof MarketIndex) {
				if (mivPane != null) {
					editorPart.setActivePage(mivpPage);
					mivPane.getScenarioViewer().setSelection(new StructuredSelection(target), true);
				}
				return;
			}
		}
	}
}
