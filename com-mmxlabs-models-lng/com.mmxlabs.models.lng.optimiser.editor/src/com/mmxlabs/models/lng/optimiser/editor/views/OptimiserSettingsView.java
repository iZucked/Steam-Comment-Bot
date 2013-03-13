/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.optimiser.ui.editorpart.OptimiserSettingsEditorPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class OptimiserSettingsView extends ScenarioTableViewerView<OptimiserSettingsEditorPane> {
	@Override
	protected OptimiserSettingsEditorPane createViewerPane() {
		return new OptimiserSettingsEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final OptimiserSettingsEditorPane pane) {
		pane.init(Arrays.asList(new EReference[] { OptimiserPackage.eINSTANCE.getOptimiserModel_Settings() }), getAdapterFactory(), getEditingDomain().getCommandStack());
		pane.getViewer().setInput(getRootObject().getSubModel(OptimiserModel.class));
	}

	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {

			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			OptimiserSettings object = null;
			if (dcsd.getTarget() instanceof OptimiserSettings) {
				object = (OptimiserSettings) dcsd.getTarget();
			}
			if (object != null) {
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(object), true);
			}
		}
	}
}
