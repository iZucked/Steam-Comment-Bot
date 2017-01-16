/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.ui.editorpart;

import java.util.Collections;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.mmxlabs.models.lng.pricing.BaseFuelIndex;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.pricing.CommodityIndex;
import com.mmxlabs.models.lng.pricing.IndexPoint;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.UnitConversion;
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

		indexPage = editorPart.addPage(indexPane.getControl());
		editorPart.setPageText(indexPage, "Markets");
		PlatformUI.getWorkbench().getHelpSystem().setHelp(indexPane.getControl(), "com.mmxlabs.lingo.doc.Editor_Markets");
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
			if (target instanceof CommodityIndex || target instanceof CharterIndex || target instanceof BaseFuelIndex) {
				return true;
			}
			if (target instanceof IndexPoint) {
				return true;
			}
			if (target instanceof UnitConversion) {
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

			if (target instanceof CommodityIndex || target instanceof CharterIndex || target instanceof BaseFuelIndex) {
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
