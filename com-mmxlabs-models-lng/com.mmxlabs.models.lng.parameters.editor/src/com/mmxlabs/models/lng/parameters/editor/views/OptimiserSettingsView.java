/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.editor.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.ui.editorpart.OptimiserSettingsEditorPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class OptimiserSettingsView extends ScenarioTableViewerView<OptimiserSettingsEditorPane> {
	@Override
	protected OptimiserSettingsEditorPane createViewerPane() {
		return new OptimiserSettingsEditorPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final OptimiserSettingsEditorPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ParametersModel(), ParametersPackage.eINSTANCE.getParametersModel_Settings() }),
					getAdapterFactory(), domain.getCommandStack());
			pane.getViewer().setInput(getRootObject());
		}
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
