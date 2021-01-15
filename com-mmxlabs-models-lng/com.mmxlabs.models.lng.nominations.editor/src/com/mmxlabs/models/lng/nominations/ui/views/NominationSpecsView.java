/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.nominations.AbstractNominationSpec;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.ui.editorpart.NominationSpecsViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NominationSpecsView extends ScenarioTableViewerView<NominationSpecsViewerPane> {
	public static final String ID = "com.mmxlabs.models.lng.nominations.editor.NominationSpecsView";
	
	@Override
	protected NominationSpecsViewerPane createViewerPane() {
		return new NominationSpecsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final NominationSpecsViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { 
					LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel(),
					NominationsPackage.eINSTANCE.getNominationsModel_NominationSpecs() }),
					getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
			pane.pack();
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
			if (dcsd.getTarget() instanceof AbstractNominationSpec) {
				final AbstractNominationSpec ns = (AbstractNominationSpec) dcsd.getTarget();
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(ns), true);
			}
		}
	}

}
