/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;

import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.ui.editorpart.NominationSpecsViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

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
					NominationsPackage.eINSTANCE.getNominationsModel_SlotNominationSpecs() }),
					getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
		}	
	}
}
