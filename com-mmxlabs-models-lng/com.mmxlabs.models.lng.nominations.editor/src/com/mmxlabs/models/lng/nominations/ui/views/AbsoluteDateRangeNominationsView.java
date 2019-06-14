/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.nominations.ui.views;
/*
import java.util.Arrays;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.nominations.NominationsPackage;
import com.mmxlabs.models.lng.nominations.ui.editorpart.AbsoluteDateRangeNominationsViewerPane;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.ui.views.ScenarioTableViewerView;

public class AbsoluteDateRangeNominationsView extends ScenarioTableViewerView<AbsoluteDateRangeNominationsViewerPane> {
	public static final String ID = "com.mmxlabs.models.lng.nominations.editor.AbsoluteDateRangeNominationsView";

	public AbsoluteDateRangeNominationsView() {
		
	}
	
	@Override
	protected AbsoluteDateRangeNominationsViewerPane createViewerPane() {
		return new AbsoluteDateRangeNominationsViewerPane(getSite().getPage(), this, this, getViewSite().getActionBars());
	}

	@Override
	protected void initViewerPane(final AbsoluteDateRangeNominationsViewerPane pane) {
		final EditingDomain domain = getEditingDomain();
		if (domain != null) {
			pane.init(Arrays.asList(new EReference[] { 
					LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel(), 
					NominationsPackage.eINSTANCE.getNominationsModel_SlotNominations() }),
					getAdapterFactory(), getModelReference());
			pane.getViewer().setInput(getRootObject());
		}
	}
}
*/