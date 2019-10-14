/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mmxlabs.models.lng.nominations.AbstractNomination;
import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.ui.editorpart.RelativeDateRangeNominationsViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.tabular.ScenarioTableViewerPane;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;
import com.mmxlabs.models.ui.validation.DetailConstraintStatusDecorator;

public class NominationsView extends ScenarioTableViewerView<ScenarioTableViewerPane> {
	
	public static final String ID = "com.mmxlabs.models.lng.nominations.editor.NominationsView";
	
	@Override
	protected ScenarioTableViewerPane createViewerPane() {
		return new RelativeDateRangeNominationsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final ScenarioTableViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { 
					LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel(), 
					NominationsPackage.eINSTANCE.getNominationsModel_Nominations() }),
					getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
			pane.pack();
		}
		
		getSite().setSelectionProvider(pane.getScenarioViewer());
	}
	
	@Override
	public void openStatus(final IStatus status) {
		if (status.isMultiStatus()) {
			// Try first element
			openStatus(status.getChildren()[0]);
		}

		if (status instanceof DetailConstraintStatusDecorator) {
			final DetailConstraintStatusDecorator dcsd = (DetailConstraintStatusDecorator) status;
			if (dcsd.getTarget() instanceof AbstractNomination) {
				final AbstractNomination n = (AbstractNomination) dcsd.getTarget();
				getSite().getPage().activate(this);
				setSelection(new StructuredSelection(n), true);
			}
		}
	}
}
